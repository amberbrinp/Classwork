def read_file(filename, array)
    file = File.new(filename, "r")
    while (line = file.gets)
        array.push line
    end
    file.close
end


read_file('Jazzonia.txt', poem=[])
puts poem.grep(/^W.*|.*OLD.*/i)
