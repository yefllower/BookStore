CREATE TABLE Publisher (
    pid     INT,
    name    VARCHAR(256) NOT NULL,
    address VARCHAR(1024),
    PRIMARY KEY (pid)
);

CREATE TABLE Writer (
    aid     INT,
    name    INT NOT NULL,
    address VARCHAR(1024),
    email   VARCHAR(256),
    PRIMARY KEY (aid),
    CHECK (email LIKE '_%@_%._%')
);

CREATE TABLE Subject (
    sid  INT,
    name INT NOT NULL,
    PRIMARY KEY (sid),
    UNIQUE (name)
);

CREATE TABLE Topic (
    kid  INT,
    name INT NOT NULL,
    PRIMARY KEY (kid),
    UNIQUE (name)
);

CREATE TABLE Book (
    bid       INT,
    isbn      CHAR(10),
    title     VARCHAR(256) NOT NULL,
    publisher INT          NOT NULL,
    year      INT          NOT NULL,
    stock     INT          NOT NULL,
    price     INT,
    format    ENUM('hardcover', 'softcover'),
    subject   INT,
    PRIMARY KEY (bid),
    FOREIGN KEY (subject) REFERENCES Subject (sid)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    UNIQUE (isbn),
    CHECK (stock >= 0),
    CHECK (price >= 0)
);

CREATE TABLE Keyword (
    kid INT,
    bid INT,
    PRIMARY KEY (kid, bid),
    FOREIGN KEY (kid) REFERENCES Topic (kid)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (bid) REFERENCES Book (bid)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE Author (
    bid INT,
    aid INT,
    PRIMARY KEY (bid, aid),
    FOREIGN KEY (bid) REFERENCES Book (bid)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (aid) REFERENCES Writer (aid)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE Customer (
    uid     INT,
    user    VARCHAR(32)  NOT NULL,
    passwd  VARCHAR(256) NOT NULL,
    name    VARCHAR(256) NOT NULL,
    address VARCHAR(1024),
    tel     CHAR(20),
    PRIMARY KEY (uid),
    UNIQUE (user)
);

CREATE TABLE Feedback (
    fid     INT,
    rid     INT,
    score   INT NOT NULL,
    comment TEXT,
    PRIMARY KEY (fid),
    UNIQUE (rid),
    CHECK (score >= 1 && score <= 10)
);

CREATE TABLE Purchase (
    rid     INT,
    orderId BIGINT NOT NULL,
    uid     INT    NOT NULL,
    bid     INT    NOT NULL,
    fid     INT,
    PRIMARY KEY (rid),
    FOREIGN KEY (uid) REFERENCES Customer (uid)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (bid) REFERENCES Book (bid)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    FOREIGN KEY (fid) REFERENCES Feedback (fid)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    UNIQUE (fid)
);

ALTER TABLE Feedback
ADD FOREIGN KEY (rid) REFERENCES Purchase (rid)
    ON UPDATE CASCADE
    ON DELETE CASCADE;

CREATE TABLE Rating (
    uid0   INT,
    uid1   INT,
    rating ENUM('useless', 'useful', 'very useful'),
    trust  BOOLEAN,
    PRIMARY KEY (uid0, uid1),
    FOREIGN KEY (uid0) REFERENCES Customer (uid)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (uid1) REFERENCES Customer (uid)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);