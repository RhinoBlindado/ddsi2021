
CREATE TABLE EDICION
(
    anno NUMBER CHECK (anno BETWEEN 2000 AND 2077) PRIMARY KEY 
);


CREATE TABLE JUGADOR
(
    idJugador NUMBER PRIMARY KEY,
    correo VARCHAR2(50) UNIQUE NOT NULL,
    nombre VARCHAR2(32) NOT NULL,
    apellidos VARCHAR2(32) NOT NULL,
    telefono VARCHAR(12),
   CHECK(correo LIKE '%___@___%'),
   CHECK(teléfono LIKE '%+___%')

);


CREATE TABLE ENTRENADOR
(
    idEntrenador NUMBER PRIMARY KEY,
    correo VARCHAR2(50) UNIQUE NOT NULL,
    nombre VARCHAR2(32) NOT NULL,
    apellidos VARCHAR2(32) NOT NULL,
    telefono VARCHAR(12),
   CHECK(correo LIKE '%___@___%'),
   CHECK(teléfono LIKE '%+___%')
);


CREATE TABLE USUARIO(
    idUsuario NUMBER PRIMARY KEY,
    correo VARCHAR2(32) UNIQUE NOT NULL,
    nombre VARCHAR2(32) NOT NULL,
    apellidos VARCHAR2(32) NOT NULL,
    telefono VARCHAR(12),
    contrasenia VARCHAR(30) NOT NULL,
    notificaciones VARCHAR2(3), 
    CHECK(correo LIKE '%___@___%'),
    CHECK(telefono LIKE '%+___%'),
    CHECK (notificaciones IN ('ON','OFF'))
    );

CREATE TABLE COMPRA_REALIZA_INICIA(
    idCompra NUMBER PRIMARY KEY,
    fechaInicio DATE NOT NULL,
    idUsuario NUMBER REFERENCES USUARIO(idUsuario) NOT NULL,
    annoEdicion NUMBER REFERENCES EDICION(anno) NOT NULL
);


CREATE TABLE COMPRA_FINALIZADA
(
    idCompra REFERENCES COMPRA_REALIZA_INICIA(idCompra) PRIMARY KEY,
    fechaFin DATE NOT NULL
);


CREATE TABLE COMPRA_PAGADA(
    idCompra REFERENCES COMPRA_FINALIZADA(idCompra) PRIMARY KEY,
    fechaPago DATE NOT NULL
);


CREATE TABLE ENTRADAS(
    idEntrada NUMBER PRIMARY KEY,
    tipo VARCHAR2(20) NOT NULL,
    CHECK (tipo IN (‘BASICA’, ‘PREMIUM’, ‘VIP’))
);


CREATE TABLE ENTIDAD
(
    idEntidad NUMBER PRIMARY KEY,
    nombreEntidad VARCHAR2(60) UNIQUE NOT NULL,
    nombreContacto VARCHAR2(80),
    correo VARCHAR2(50) UNIQUE NOT NULL,
    telefono VARCHAR(12) NOT NULL,
    CHECK(correo LIKE '%___@___%'),
    CHECK(telefono LIKE '%+___%')
);





CREATE TABLE PERSONAL(
    idPersonal NUMBER PRIMARY KEY,
    correo VARCHAR2(32) UNIQUE NOT NULL,
    nombre VARCHAR2(32) NOT NULL,
    apellidos VARCHAR2(32) NOT NULL,
    telefono VARCHAR(12),
    CHECK(correo LIKE '%___@___%'),
    CHECK(telefono LIKE '%+___%')
    );


CREATE TABLE PISTA
(
    idPista NUMBER PRIMARY KEY,
    nombre VARCHAR2(60),
    capacidad NUMBER(5) NOT NULL,
    CHECK(capacidad>0)
);


CREATE TABLE PEDIDO(
    numPedido NUMBER PRIMARY KEY,
    estado VARCHAR(20) CHECK (estado IN ('INICIADO','FINALIZADO', 'PAGADO', 'RECIBIDO')) NOT NULL
);


CREATE TABLE TIENE(
    idEntrada NUMBER REFERENCES ENTRADAS (idEntrada),
    anno NUMBER REFERENCES EDICION(anno),
    precio FLOAT NOT NULL,
    cantidad NUMBER NOT NULL,

    PRIMARY KEY(idEntrada, anno),
    CHECK(precio > 0 AND cantidad > 0)
);


CREATE TABLE ANADE
(
    anno NUMBER,
    idCompra NUMBER REFERENCES COMPRA_REALIZA_INICIA(idCompra),
    idEntrada NUMBER,
    Cantidad NUMBER NOT NULL,
    FOREIGN KEY (anno, idEntrada) REFERENCES TIENE(anno, idEntrada),
    PRIMARY KEY(anno, idCompra, idEntrada),
    CHECK (Cantidad > 0)
);


CREATE TABLE PATROCINA(
    idEntidad NUMBER REFERENCES ENTIDAD(idEntidad),
    anno NUMBER REFERENCES EDICION(anno),
    cantidadDinero FLOAT NOT NULL,
    PRIMARY KEY (idEntidad,anno),
    CHECK(cantidadDinero > 0)
    );


CREATE TABLE COLABORA
(
    idEntidad REFERENCES ENTIDAD(idEntidad),
    anno REFERENCES EDICION(anno),
    cantidadDinero FLOAT NOT NULL,
    
    PRIMARY KEY(idEntidad, anno),
    CHECK(cantidadDinero > 0)
);


CREATE TABLE TRABAJA(
    idPersonal NUMBER REFERENCES PERSONAL(idPersonal),
    anno NUMBER REFERENCES EDICION(anno),
    sueldoHora FLOAT CHECK(sueldoHora >= 4) NOT NULL,
    horas NUMBER CHECK (horas >= 1 AND horas <= 8) NOT NULL,
    PRIMARY KEY(idPersonal, anno)
);


CREATE TABLE ES_ASIGNADO(
    idPersonal REFERENCES PERSONAL(idPersonal),
    anno REFERENCES EDICION(anno),
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    idPista REFERENCES PISTA(idPista),

    CHECK(fecha_inicio <= fecha_fin),
    PRIMARY KEY(idPersonal, anno, fecha_inicio, fecha_fin)
);



CREATE TABLE MATERIAL_PROPORCIONADO(
    idMaterial NUMBER PRIMARY KEY,
    nombreMaterial VARCHAR2(30) NOT NULL,
    idEntidad NUMBER,
    anno NUMBER,

    FOREIGN KEY(idEntidad, anno) REFERENCES PATROCINA(idEntidad, anno)
);


CREATE TABLE PIDE
(
    numPedido NUMBER REFERENCES PEDIDO(numPedido),
    idMaterial NUMBER REFERENCES MATERIAL_PROPORCIONADO(idMaterial),
    cantidad NUMBER NOT NULL,
    PRIMARY KEY(numPedido, idMaterial),
    CHECK (cantidad > 0)
);


   CREATE TABLE CONSIGNADO (
    numPedido NUMBER REFERENCES PEDIDO(numPedido),
    idPersonal NUMBER  ,
    anno NUMBER ,
    fecha_inicio DATE ,
    fecha_fin DATE,
    FOREIGN KEY(idPersonal,anno,fecha_inicio,fecha_fin) 
    REFERENCES ES_ASIGNADO (idPersonal,anno,fecha_inicio,fecha_fin),
    PRIMARY KEY(numPedido,idPersonal,anno,fecha_inicio,fecha_fin)
    );



CREATE TABLE EMPAREJA
(
    idJugador1 REFERENCES JUGADOR(idJugador),
    idJugador2 REFERENCES JUGADOR(idJugador),
    
    PRIMARY KEY (idJugador1, idJugador2),
    CHECK(idJugador1 != idJugador2)
);






CREATE TABLE PARTICIPA_ENTRENA
(
    anno NUMBER REFERENCES EDICION(anno),
    idJugador1 NUMBER,
    idJugador2 NUMBER,
    idEntrenador NUMBER REFERENCES ENTRENADOR(idEntrenador) NOT NULL,
    Ranking NUMBER NOT NULL,
    FOREIGN KEY (idJugador1, idJugador2) REFERENCES EMPAREJA(idJugador1, idJugador2),
    PRIMARY KEY(anno, idJugador1, idJugador2),
    CHECK (Ranking > 0)
);
 
