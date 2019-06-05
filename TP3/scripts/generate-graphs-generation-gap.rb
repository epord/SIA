GLOB = "D:/Users/Juan/Google Drive/ITBA Compartido/SIA/TP3/GenerationGap/**/out.m"

# Read all fitnesses first
fs = {}
Dir.glob(GLOB).each do |file|
	bestFitness = File.foreach(file).first.split(" ")[-2].to_f
	fs[file] = bestFitness
end
sorted_fs = fs.sort_by { |_,v| v }.map { |filename, _| filename }.reverse

# Now generate graphs
mega = ""
Dir.glob(GLOB).each do |file|
	filename = "#{sorted_fs.index(file)+1}_" + File.dirname(file).split("/")[-1] + ".jpg"
	
  mega << "clf;\n" + File.read(file) + "\nprint(\"#{filename}\", \"-djpg\");\n\n"
end
mega << "exit;\n"
File.write("D:/Users/Juan/Google Drive/ITBA Compartido/SIA/TP3/GenerationGap/mega.m", mega)
puts "Done, now run it"
