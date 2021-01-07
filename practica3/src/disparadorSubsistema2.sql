-- Subsistema 2 - Usuarios / Entradas
-- Disparador para evitar que se realice más de una compra al día

CREATE OR REPLACE TRIGGER unaCompraPorDia
	BEFORE
	INSERT ON COMPRA_FINALIZADA
	FOR EACH ROW

DECLARE
	sameDay INTEGER;
BEGIN
	SELECT count(*) INTO sameDay FROM COMPRA_FINALIZADA WHERE TO_CHAR(FECHAFIN,'DD-MM-YYYY') = TO_CHAR(:new.FECHAFIN,'DD-MM-YYYY');
	IF (sameDay > 1) THEN
		raise_application_error(-666, :new.FECHAFIN || 'Un usuario no puede realizar más de una compra por día.');
	END IF;
END; 