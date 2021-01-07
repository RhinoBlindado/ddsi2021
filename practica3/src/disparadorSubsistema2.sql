-- Subsistema 2 - Usuarios / Entradas
-- Disparador para evitar que se realice más de una compra al día

CREATE OR REPLACE TRIGGER SUBSIS2__COMPRA_POR_DIA
	BEFORE
	INSERT ON COMPRA_PAGADA
	FOR EACH ROW

DECLARE
	sameDayCount INTEGER;
    idUsr INTEGER;
BEGIN
    SELECT idusuario INTO idUsr FROM COMPRA_REALIZA_INICIA WHERE idcompra = :new.idcompra;
    SELECT COUNT(*) INTO sameDayCount FROM COMPRA_PAGADA WHERE TO_CHAR(FECHAPAGO,'DD-MM-YYYY') = TO_CHAR(:new.FECHAPAGO,'DD-MM-YYYY') 
        AND idcompra IN (SELECT idcompra FROM COMPRA_REALIZA_INICIA WHERE idUsuario = idUsr);
	IF (sameDayCount > 1) THEN
		raise_application_error(-20666,'Usuario ' || idUsr || ' ha intentado realizar más de una compra por día: ' || :new.FECHAPAGO);
	END IF;
END; 