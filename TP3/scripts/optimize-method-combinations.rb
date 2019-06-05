require 'fileutils'

maxF = 0
fs = {}
# Dir.glob("D:/Users/Juan/Google Drive/ITBA Compartido/SIA/TP3/Mixed/**/out.m").each do |file|
Dir.glob("D:/SIA/TP3/runs/mixed-optimize/**/settings.properties").each do |file|
	config = File.read(file)
    				.sub(/^end.condition.type ?=.*$/, "end.condition.type=0")
    				.sub(/^max.generations ?=.*$/, "max.generations=25000")
    File.write(file, config)

    dir = File.dirname(file)
    puts "Running #{dir}..."
    system("cd #{dir} && java -Xmx4G -jar D:/SIA/TP3/target/genetics-1.0-SNAPSHOT.jar")
end

puts "DONE"
