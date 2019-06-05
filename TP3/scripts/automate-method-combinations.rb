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

[0.1, 0.25, 0.5, 0.75].each do |a|
	[0.1, 0.25, 0.5, 0.75].each do |b|
		# [[1, 2], [3, 4], [6, 2, 7, 2]].each do |methods|
		[[6, 2, 7, 2]].each do |methods|
			index = 0
			method1 = [methods[index]]
			index += 1
			if requires_second(method1[0])
				method1 << methods[index]
				index += 1
			end
			method2 = [methods[index]]
			index += 1
			if requires_second(method2[0])
				method2 << methods[index]
				index += 1
			end

			dir = "D:/SIA/TP3/runs/#{method_mappings[method1[0]]}_#{a}_#{method_mappings[method2[0]]}_#{b}"
			# dir = "D:/Users/Juan/Google Drive/ITBA Compartido/SIA/TP3/Mixed/#{method_mappings[method1[0]]}_#{a}_#{method_mappings[method2[0]]}_#{b}"
			FileUtils.mkdir_p dir
			
			config = File.read("D:/SIA/TP3/example.properties")
				.sub(/^crossover.selection.A=.*$/, "crossover.selection.A=#{a}")
				.sub(/^crossover.selection.method1=.*$/, "crossover.selection.method1=#{method1[0]}")
				.sub(/^crossover.selection.method2=.*$/, "crossover.selection.method2=#{method2[0]}")
				.sub(/^replacement.selection.B=.*$/, "replacement.selection.B=#{b}")
				.sub(/^replacement.selection.method1=.*$/, "replacement.selection.method1=#{method1[0]}")
				.sub(/^replacement.selection.method2=.*$/, "replacement.selection.method2=#{method2[0]}")

			if method1.length > 1 then
				config = config.sub(/^crossover.selection.second.method1=.*$/, "crossover.selection.second.method1=#{method1[1]}")
			end
			if method2.length > 1 then
				config = config.sub(/^replacement.selection.second.method2=.*$/, "replacement.selection.second.method2=#{method2[1]}")
			end

			File.write("#{dir}/settings.properties", config)

			puts "Running #{dir}..."
			spawn("cd #{dir} && java -Xmx1G -jar D:/SIA/TP3/target/genetics-1.0-SNAPSHOT.jar")
		end
	end
end

puts "Waiting for all to finish..."
Process.waitall
puts "DONE"
