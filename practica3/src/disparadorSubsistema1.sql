CREATE OR REPLACE TRIGGER rankingcorrecto
FOR UPDATE OF RANKING ON PARTICIPA_ENTRENA
COMPOUND TRIGGER
/*Creamos una "subtabla" con los datos que necesitamos*/
TYPE tiporankin IS RECORD(
    rankk PARTICIPA_ENTRENA.ranking%TYPE,
    edic PARTICIPA_ENTRENA.anno%TYPE
    );
/*Hacemos que esa "subtabla" sea un tipo*/
TYPE tablarank is TABLE OF tiporankin INDEX BY BINARY_INTEGER;
/*Creamos un objeto de ese tipo*/
datos_rank tablarank;
/*Creamos un cursor para recorrer la tabla participa entrena*/
CURSOR cosas IS SELECT anno,ranking FROM PARTICIPA_ENTRENA;
existe NUMBER;
/*Hacemos un before statement que nos guarde todos los datos que necesitamos en nuestro nueva "subtabla"*/
BEFORE STATEMENT IS
    i BINARY_INTEGER := 1;
BEGIN
/*Movemos el cursor por toda la tabla almacenando en la nueva "subtabla" los datos que nos interesan */
    FOR dato IN cosas LOOP
        datos_rank(i).rankk := dato.ranking;
        datos_rank(i).edic := dato.anno;
        i := i + 1;
    END LOOP;
END BEFORE STATEMENT;

BEFORE EACH ROW IS
    i BINARY_INTEGER := 1;
BEGIN
/*Recorremos la "subtabla" comprobando si existe en la edición en la que estamos interesados alguien que ya tenga el ranking al que vamos a actualizar*/
    FOR i IN 1..datos_rank.COUNT LOOP
        IF datos_rank(i).rankk = :new.ranking AND datos_rank(i).edic = :new.anno THEN
            raise_application_error(-20000, :new.ranking || ' No pueden existir parejas con el mismo ranking');
        END IF;
    END LOOP;
END BEFORE EACH ROW;
END;
