#!/bin/ruby

`cp architecture.conf architecture.conf.bak`
`rm configurationResults.txt > /dev/null 2>&1`

file_name = "architecture.conf"
(0.1..0.9).step(0.1).each do |learningFactor|
    puts "Testing with learning rate #{learningFactor}"
    sleep 1
    contents = File.read(file_name)
    new_contents = contents.sub(/learningFactor = .+/, "learningFactor = #{learningFactor};")
    puts new_contents
    gets
    File.open(file_name, "w") {|file| file.puts new_contents }
    `octave multiLayerPerceptron.m --silent --auto-exit | tee -a configurationResults.txt`
end
`cp architecture.conf.bak architecture.conf`
`rm architecture.conf.bak`