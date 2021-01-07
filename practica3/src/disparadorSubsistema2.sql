-- Subsistema 2 - Usuarios / Entradas
-- Disparador para evitar que se realice más de una compra al día

CREATE OR REPLACE TRIGGER unaCompraPorDia
	BEFORE
	INSERT ON COMPRA_FINALIZADA
	FOR EACH ROW

DECLARE
	sameDayCount INTEGER;
    idUsr INTEGER;
BEGIN
    SELECT idusuario INTO idUsr FROM COMPRA_REALIZA_INICIA WHERE idcompra = :new.idcompra;
    SELECT COUNT(*) INTO sameDayCount FROM COMPRA_FINALIZADA WHERE TO_CHAR(FECHAFIN,'DD-MM-YYYY') = TO_CHAR(:new.FECHAFIN,'DD-MM-YYYY') 
        AND idcompra IN (SELECT idcompra FROM COMPRA_REALIZA_INICIA WHERE idUsuario = idUsr);
	IF (sameDayCount > 1) THEN
		raise_application_error(-20666,'Usuario' || idUsr || 'ha intentado realizar más de una compra por día: ' || :new.FECHAFIN);
	END IF;
END; 