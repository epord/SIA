#!/bin/ruby

`cp architecture.conf architecture.conf.bak`
`rm configurationResults.txt > /dev/null 2>&1`

file_name = "architecture.conf"
[0.21, 0.22, 0.23, 0.24, 0.25, 0.26, 0.27, 0.28, 0.29].each do |learningFactor|
    puts "Testing with learning rate #{learningFactor}"
    sleep 1
    contents = File.read(file_name)
    new_contents = contents.sub(/learningFactor = .+/, "learningFactor = #{learningFactor};")
    File.open(file_name, "w") {|file| file.puts new_contents }
    `octave multiLayerPerceptron.m --silent --auto-exit | tee -a configurationResults.txt`
end
`cp architecture.conf.bak architecture.conf`
`rm architecture.conf.bak`