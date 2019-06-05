Dir.glob("D:/Users/Juan/Google Drive/ITBA Compartido/SIA/TP3/GenerationGap/**/out.m").each do |file|
# Dir.glob("D:/SIA/TP3/runs/**/out.m").each do |file|
# Dir.glob("D:/SIA/TP3/runs/mixed-optimize/**/out.m").each do |file|
	first_line_chunks = File.foreach(file).first.split(" ")

  num_generations = first_line_chunks.length - 3
  best_fitness = first_line_chunks[-2].to_f

	dir = File.dirname(file)
	chunks = dir.split('/')[-1].split("_")
	gap = chunks[-1]

	puts "#{gap},#{best_fitness},#{num_generations}"
end
