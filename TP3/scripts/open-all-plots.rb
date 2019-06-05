BASE_PATH = "D:/SIA/TP3/runs"

result = ""
Dir.glob("#{BASE_PATH}/**/out.m").each_with_index do |file, i|
    result << "figure(#{i+1});\n" + File.read(file) + "text(0,0, \"#{file}\");\n\n"
end

File.write("#{BASE_PATH}/mega.m", result)

puts "Done, run #{BASE_PATH}/mega.m in Octave to open all plots"
