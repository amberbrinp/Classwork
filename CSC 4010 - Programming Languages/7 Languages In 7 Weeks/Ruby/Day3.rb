class ActsAsCsv
  def read
    file = File.new(self.class.to_s.downcase + '.txt')
    @headers = file.gets.chomp.split(', ')

    file.each do |row|
      @result << row.chomp.split(', ')
    end
  end
  
  def headers
    @headers
  end
  
  def csv_contents
    @result
  end
  
  def initialize
    @result = []
    read
  end
  
  def each(&block)
      csv_contents.each { |row| block.call CsvRow.new(self, row) }
  end
end

class RubyCsv < ActsAsCsv
end  

class CsvRow
    def initialize(m, contents)
        @csv = m
        @contents = contents
    end
    def method_missing(i, *args)
        contentIndex = @csv.headers.index i.to_s
        @contents[contentIndex] if contentIndex != nil
    end
    def respond_to?(n)
        @csv.headers.index n.to_s
    end
end

csv = RubyCsv.new
csv.each {|row| puts row.one}
puts csv.headers.inspect
puts csv.csv_contents.inspect

#TODO:
#For the file:
#one, two
#lions, tigers

#allow an API that works like this:
#csv = RubyCsv.new
#csv.each {|row| puts row.one}
#This should print "lions".
