SET PATH=%PATH%;"F:\Files\actual files\Java\jdk8.171\bin"
cd bin
if exist *.class del *.class
cd..
cls
javac src/*.java -d bin
pause
cd bin
cls
java RunFlowingFlowcharts