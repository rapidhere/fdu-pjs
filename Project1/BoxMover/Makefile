all: BoxMover

JC=javac
FLAG=-verbose -encoding utf-8

BoxMover: doc1 maps1 save1 src1
	mkdir BoxMover
	mv doc1 ./BoxMover/doc
	mv maps1 ./BoxMover/maps
	mv save1 ./BoxMover/save
	mv ./src1/* ./BoxMover/
	rm ./src1 -r
	
doc1: 
	[ -d ./doc ] && \
	cp -r ./doc ./doc1 || \
	exit 1 

maps1:
	[ -d ./maps ] && \
	cp -r ./maps ./maps1 || \
	exit 1 

save1:
	[ -d ./save ] && \
	mkdir save1 && \
	rm -f ./save1/empty || \
	exit 1 

src1:
	[ -d ./src ] && \
	mkdir src1 && \
	cd src && \
	$(JC) BoxMover.java -d ../src1 $(FLAG) \
	&& cd .. || \
	exit 1 

