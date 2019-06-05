maxF = 0
fs = {}
# Dir.glob("D:/Users/Juan/Google Drive/ITBA Compartido/SIA/TP3/Mixed/**/out.m").each do |file|
# Dir.glob("D:/SIA/TP3/runs/**/out.m").each do |file|
Dir.glob("D:/SIA/TP3/runs/mixed-optimize/**/out.m").each do |file|
	bestFitness = File.foreach(file).first.split(" ")[-2].to_f
	fs[file] = bestFitness
	maxF = bestFitness > maxF ? bestFitness : maxF
	puts "Fitness in #{file} = #{bestFitness}"
end
puts
puts maxF
puts fs.sort_by { |k,v| v }
