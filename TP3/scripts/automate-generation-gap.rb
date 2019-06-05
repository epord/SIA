require 'fileutils'

method_mappings = {
	1 => 'elite',
	2 => 'roulette',
	3 => 'universal',
	4 => 'deterministic-tournament',
	5 => 'probabilistic-tournament',
	6 => 'ranking',
	7 => 'boltzmann',
}

def requires_second(method_num)
	method_num == 6 || method_num == 7
end

[0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0].each do |generation_gap|
	dir = "D:/SIA/TP3/runs/gen-gap_#{generation_gap}"
	# dir = "D:/Users/Juan/Google Drive/ITBA Compartido/SIA/TP3/Mixed/#{method_mappings[method1[0]]}_#{a}_#{method_mappings[method2[0]]}_#{b}"
	FileUtils.mkdir_p dir
	
	config = File.read("D:/SIA/TP3/example.properties")
		.sub(/^crossover.selection.A=.*$/, "crossover.selection.A=0.5")
		.sub(/^crossover.selection.method1=.*$/, "crossover.selection.method1=3")
		.sub(/^crossover.selection.method2=.*$/, "crossover.selection.method2=4")
		.sub(/^replacement.selection.B=.*$/, "replacement.selection.B=0.75")
		.sub(/^replacement.selection.method1=.*$/, "replacement.selection.method1=3")
		.sub(/^replacement.selection.method2=.*$/, "replacement.selection.method2=4")
		.sub(/^replacement.generationGap=.*$/, "replacement.generationGap=#{generation_gap}")

	File.write("#{dir}/settings.properties", config)

	puts "Running #{dir}..."
	spawn("cd #{dir} && java -Xmx2G -jar D:/SIA/TP3/target/genetics-1.0-SNAPSHOT.jar")
end

puts "Waiting for all to finish..."
Process.waitall
puts "DONE"
