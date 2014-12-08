all:
	javac src/bookstore/*.java
run: all
	cd src; java -cp ../lib/mysql.jar:. bookstore.Main
gen: src/gen.cpp
	g++ src/gen.cpp -o genData
	./genData > src/test1.in
	rm genData
test1: all gen
	cd src; java -cp ../lib/mysql.jar:. bookstore.Main < ./test1.in
