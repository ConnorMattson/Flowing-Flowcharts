SET PATH="%CD:~0,3%Files\actual files\Java\jdk8.171\bin";%PATH%
cd bin
if exist *.class del *.class
cd..
cls
javac -target 1.8 src/*.java -d bin
pause
cd bin
cls
java RunFlowingFlowcharts