all:
	javac src/bookstore/*.java
run: all
	cd src; java -cp ../lib/mysql.jar:. bookstore.Main
test: all
	cd src; java -cp ../lib/mysql.jar:. bookstore.Main < ./test.in
