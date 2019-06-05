method_mappings = {
	"elite" => "Elite",
	"universal" => "Universal",
	"roulette" => "Ruleta",
	"deterministic-tournament" => "Torneo Determinístico",
	"ranking" => "Ranking + ruleta",
	"boltzmann" => "Boltzmann + ruleta",
}

Dir.glob("D:/Users/Juan/Google Drive/ITBA Compartido/SIA/TP3/Mixed/run1/**/out.m").each do |file|
# Dir.glob("D:/SIA/TP3/runs/**/out.m").each do |file|
# Dir.glob("D:/SIA/TP3/runs/mixed-optimize/**/out.m").each do |file|
	first_line_chunks = File.foreach(file).first.split(" ")

  num_generations = first_line_chunks.length - 3
  best_fitness = first_line_chunks[-2].to_f

	dir = File.dirname(file)
	chunks = dir.split('/')[-1].split("_")
	m1 = method_mappings[chunks[0].sub('[BEST]', '')]
	a = chunks[1]
	m2 = method_mappings[chunks[2]]
	b = chunks[3]

	puts "#{m1},#{m2},#{a},#{b},#{best_fitness},#{num_generations}"
end
