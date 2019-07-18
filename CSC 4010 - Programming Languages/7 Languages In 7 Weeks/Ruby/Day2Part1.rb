def print_each
    a = [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15]
    b=[]
    a[(0..15)].each do |i|
        b.push i
        if b[3]
            puts "#{b}"
            b.clear
        end
    end
end

def print_each_slice
    (0..15).each_slice(4) {|i| p i}
end

puts 'Using each:'
print_each

puts 'Using each_slice:'
print_each_slice
