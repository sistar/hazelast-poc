doesntWork = new File(".").getCanonicalPath()
println doesntWork
new File("input_data/test.txt").eachLine { line -> println(line) }