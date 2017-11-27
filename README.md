Fast and dirty compilation:
Linux:
```$ javac -cp ".:lib/*" *.java */*.java */*/*.java```
Windows:
```$ javac -cp ".;lib/*" *.java */*.java */*/*.java```

Run with:
```$ java -cp ".:lib/*" Main [iterations] [instance path] [output files prefix]```

For example:
Linux:
```$ java -cp ".:lib/*" Main 5000 instances/qapUni.75.0.1.qap.txt qapUni.75.0.1.qap```
Windows:
```$ java -cp ".;lib/*" Main 5000 instances/qapUni.75.0.1.qap.txt qapUni.75.0.1.qap```
