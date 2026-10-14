-- Un voto por usuario y mazmorra; valor +1 (up) o -1 (down).
CREATE TABLE IF NOT EXISTS public.voto (
    mazmorra bigint NOT NULL,
    usuario uuid NOT NULL,
    valor smallint NOT NULL CONSTRAINT voto_valor_check CHECK (valor IN (1, -1)),
    CONSTRAINT voto_pkey PRIMARY KEY (mazmorra, usuario),
    CONSTRAINT voto_mazmorra_fk FOREIGN KEY (mazmorra) REFERENCES public.mazmorra (id) ON DELETE CASCADE,
    CONSTRAINT voto_usuario_fk FOREIGN KEY (usuario) REFERENCES public.usuario (id) ON DELETE CASCADE
);
