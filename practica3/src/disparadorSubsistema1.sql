CREATE OR REPLACE TRIGGER PROOOBANDO
FOR UPDATE OF RANKING ON PARTICIPA_ENTRENA
COMPOUND TRIGGER
TYPE tiporankin IS RECORD(
    rankk PARTICIPA_ENTRENA.ranking%TYPE,
    edic PARTICIPA_ENTRENA.anno%TYPE
    );
    
TYPE tablarank is TABLE OF tiporankin INDEX BY BINARY_INTEGER;
datos_rank tablarank;
CURSOR cosas IS SELECT anno,ranking FROM PARTICIPA_ENTRENA;
existe NUMBER;

BEFORE STATEMENT IS
    i BINARY_INTEGER := 1;
BEGIN
    FOR dato IN cosas LOOP
        datos_rank(i).rankk := dato.ranking;
        datos_rank(i).edic := dato.anno;
        i := i + 1;
    END LOOP;
END BEFORE STATEMENT;

BEFORE EACH ROW IS
    i BINARY_INTEGER := 1;
BEGIN
    FOR i IN 1..datos_rank.COUNT LOOP
        IF datos_rank(i).rankk = :new.ranking AND datos_rank(i).edic = :new.anno THEN
            raise_application_error(-20000, :new.ranking || ' No pueden existir parejas con el mismo ranking');
        END IF;
    END LOOP;
END BEFORE EACH ROW;
END;
