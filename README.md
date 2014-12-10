BookStore
=========

A database homework project.


Intro
=========
gen.cpp is a random generator, 
it can generates funny books and operations like human input.
You can try
```bash
./gen > test.in 
make run < test.in
```

init.mysql and drop.mysql are create sentences and drop tables.
They are used to install and uninstall the database. 

Usages
=========
To compile:
```bash
make
```

To run:
```bash
make run
```

To run a siily test from test1.in:
```bash
make test1
```

To initialize the database, 
go to dir "/src"
login with mysql and type

```sql
source init.mysql
```

To clear the database, drop all tables,
go to dir "/src"
login with mysql and type

```sql
source drop.mysql
```

