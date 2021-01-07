-- Subsistema 2 - Usuarios / En
-- Disparador para evitar que se realice más de una compra al día

CREATE OR REPLACE TRIGGER unaCompraPorDia
	BEFORE
	INSERT ON COMPRA_FINALIZADA
	FOR EACH ROW

DECLARE
	sameDay INTEGER;
BEGIN
	SELECT count(*) INTO sameDay FROM COMPRA_FINALIZADA WHERE TO_CHAR(FECHA_PAGO,'DD-MM-YYYY') = TO_CHAR(:new.FECHA_PAGO,'DD-MM-YYYY');
	IF (sameDay > 1) THEN
		raise_application_error(-666, :new.FECHA_PAGO || 'Un usuario no puede realizar más una compra por día.')
	END IF;
END; 