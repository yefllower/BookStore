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
copy: all
	sudo cp src/bookstore/*.class /var/lib/tomcat6/webapps/t/WEB-INF/classes/bookstore
	sudo cp src/bookstore/*.java /var/lib/tomcat6/webapps/t/WEB-INF/classes/bookstore
	sudo cp public_html/*.jsp /var/lib/tomcat6/webapps/t/
	sudo cp public_html/*.html /var/lib/tomcat6/webapps/t/
