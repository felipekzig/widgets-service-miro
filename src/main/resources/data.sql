DROP TABLE IF EXISTS widgets;
	 
CREATE TABLE widgets (
    id          VARCHAR(36) NOT NULL,
    width       INT NOT NULL,
    height      INT NOT NULL,
    coords_x    INT NOT NULL,
    coords_y    INT NOT NULL,
    zIndex      INT NOT NULL,
    modifiedAt  TIMESTAMP WITH TIME ZONE NOT NULL,
);