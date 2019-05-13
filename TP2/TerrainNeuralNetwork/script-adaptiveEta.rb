#!/bin/ruby

`cp architecture.conf architecture.conf.bak`
`rm configurationResults.txt > /dev/null 2>&1`

file_name = "architecture.conf"
[0.05, 0.0875, 0.125].each do |increaseConstant|
	[0.1, 0.25, 0.5].each do |decreaseFactor|
	    puts "Testing with increase constant #{increaseConstant} and decrease factor #{decreaseFactor}"
	    sleep 1
	    contents = File.read(file_name)
	    new_contents = contents.
	    				sub(/learningFactorIncreaseConstant = .+/, "learningFactorIncreaseConstant = #{increaseConstant};").
	    				sub(/learningFactorDecreaseFactor = .+/, "learningFactorDecreaseFactor = #{decreaseFactor};")
	    File.open(file_name, "w") {|file| file.puts new_contents }
	    `octave multiLayerPerceptron.m --silent --auto-exit | tee -a configurationResults.txt`
	end
end
`cp architecture.conf.bak architecture.conf`
`rm architecture.conf.bak`