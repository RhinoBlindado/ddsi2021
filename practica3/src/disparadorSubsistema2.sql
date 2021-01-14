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

	-- Obtener el ID del Usuario de la compra que está por comprarse.
    SELECT idusuario INTO idUsr FROM COMPRA_REALIZA_INICIA WHERE idcompra = :new.idcompra;

	-- Determinar y contar si existe ya una tupla en la tabla COMPRA_PAGADA que sea del mismo día de la insersión nueva y que el 
	-- ID de Compra coincida con un ID de una compra iniciada por el mismo usuario, lo que indicaría que está intentando realizar
	-- más de una compra al día.
    SELECT COUNT(*) INTO sameDayCount FROM COMPRA_PAGADA WHERE TO_CHAR(FECHAPAGO,'DD-MM-YYYY') = TO_CHAR(:new.FECHAPAGO,'DD-MM-YYYY') 
        AND idcompra IN (SELECT idcompra FROM COMPRA_REALIZA_INICIA WHERE idUsuario = idUsr);

	-- Si existe una tupla entonces la nueva insersión es segunda compra en el día y lanzar la excepción.
	IF (sameDayCount > 0) THEN
		raise_application_error(-20666,'Usuario ' || idUsr || ' ha intentado realizar más de una compra por día: ' || :new.FECHAPAGO);
	END IF;
END; 