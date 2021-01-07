CREATE OR REPLACE TRIGGER rankingcorrecto
    BEFORE
     INSERT OR UPDATE ON PARTICIPA_ENTRENA
    
    FOR EACH ROW
    
    DECLARE
        cosa INTEGER;
    BEGIN
        SELECT COUNT(*) INTO cosa FROM PARTICIPA_ENTRENA WHERE PARTICIPA_ENTRENA.anno = :new.anno AND PARTICIPA_ENTRENA.ranking = :new.ranking;
           IF(cosa > 0) THEN
                raise_application_error(-20600, :new.ranking || ' No pueden existir parejas con el mismo ranking');
	END IF;
   
END;
