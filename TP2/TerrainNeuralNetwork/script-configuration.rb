#!/bin/ruby

`cp architecture.conf architecture.conf.bak`
`rm configurationResults.txt > /dev/null 2>&1`

file_name = "architecture.conf"
(2..30).step(8).each do |layer1|
    (2..30).step(8).each do |layer2|
        (2..30).step(8).each do |layer3|
            puts "Testing with [2, #{layer1}, #{layer2}, #{layer3}, 1]"
            sleep 1
            contents = File.read(file_name)
            new_contents = contents.sub(/UnitsQuantity = .+/, "UnitsQuantity = [2, #{layer1}, #{layer2}, #{layer3}, 1];")
            File.open(file_name, "w") {|file| file.puts new_contents }
            `octave multiLayerPerceptron.m --silent --auto-exit | tee -a configurationResults.txt`
        end
    end
end
`cp architecture.conf.bak architecture.conf`
`rm architecture.conf.bak`