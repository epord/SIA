require 'fileutils'

crossover_mappings = {
    0 => "one-point",
    1 => "two-point",
    2 => "annular",
    3 => "uniform",
}

[0, 1].each do |method|
    4.times do |i|
        dir = "D:/SIA/TP3/runs/crossover/#{crossover_mappings[method]}/%02d" % [i+2]
        FileUtils.mkdir_p dir

        config = File.read("D:/SIA/TP3/example.properties")
            .sub(/^crossover.type ?=.*$/, "crossover.type=#{method}")

        File.write("#{dir}/settings.properties", config)

        puts "Running #{dir}..."
        spawn("cd #{dir} && java -Xmx2G -jar D:/SIA/TP3/target/genetics-1.0-SNAPSHOT.jar")
    end
end

puts "Waiting for all to finish..."
Process.waitall
puts "DONE"
