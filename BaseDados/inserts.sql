INSERT INTO Player(name,email,password,countryCode) VALUES("BOT Easy", "boteasy@gmail.com", "", "");
insert into statistics(player_id, gamesPlayed, gamesWon, gamesLost) values(1, 3, 0, 3);


INSERT INTO Player(name,email,password,countryCode) VALUES("BOT Medium", "botmedium@gmail.com", "", "");
insert into statistics(player_id, gamesPlayed, gamesWon, gamesLost) values(2, 4, 2, 2);


INSERT INTO Player(name,email,password,countryCode) VALUES("BOT Hard", "bothard@gmail.com", "", "");
insert into statistics(player_id, gamesPlayed, gamesWon, gamesLost) values(3, 5, 4, 1);



-- Ganhou 2 medium, 1 hard, Perdeu 2 hards
INSERT INTO Player(name,email,password,countryCode) VALUES("tomas", "tomas@gmail.com", "pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=", "PT");
insert into statistics(player_id, gamesPlayed, gamesWon, gamesLost) values(4, 5, 3, 2);


insert INTO game(winner,loser,duration) values(4,2,360);
insert INTO game(winner,loser,duration) values(4,2,300);
insert INTO game(winner,loser,duration) values(4,3,450);
insert INTO game(winner,loser,duration) values(3,4,250);
insert INTO game(winner,loser,duration) values(3,4,465);

-- Ganhou 1 easy, perdeu 2 hards
INSERT INTO Player(name,email,password,countryCode) VALUES("Utilizador 1", "utilizador1@gmail.com", "", "PT");
insert into statistics(player_id, gamesPlayed, gamesWon, gamesLost) values(5, 3, 1, 2);


-- Ganhou 2 easys e perdeu 2 medium
INSERT INTO Player(name,email,password,countryCode) VALUES("Utilizador 2", "utilizador2@gmail.com", "", "FR");
insert into statistics(player_id, gamesPlayed, gamesWon, gamesLost) values(6, 3, 3, 0);



