require 'fileutils'

# Dir.glob("D:/Users/Juan/Google Drive/ITBA Compartido/SIA/TP3/Mixed/**/out.m").each do |file|
Dir.glob("D:/SIA/TP3/runs/**/out.m").each do |file|
	puts "Running #{dir}..."
    system("cd #{dir} && java -Xmx4G -jar D:/SIA/TP3/target/genetics-1.0-SNAPSHOT.jar")
end

puts "DONE"
