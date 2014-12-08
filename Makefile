all:
	javac src/bookstore/*.java
run: all
	cd src; java -cp ../lib/mysql.jar:. bookstore.Main
test1: all
	cd src; java -cp ../lib/mysql.jar:. bookstore.Main < ./test1.in
