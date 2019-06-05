maxG = 0
Dir.glob("D:/SIA/TP3/runs/**/out.m").each do |file|
	numGenerations = File.foreach(file).first.split(" ").length - 3
	maxG = numGenerations > maxG ? numGenerations : maxG
	puts "Generations in #{file} = #{numGenerations}"
end
puts
puts maxG
