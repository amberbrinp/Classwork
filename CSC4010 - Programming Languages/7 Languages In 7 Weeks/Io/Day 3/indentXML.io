// Enhance the XML program to add spaces to show the indentation structure.

Builder := Object clone
Builder nest := 0
Builder tab := method( "    " repeated(nest) )

Builder forward := method(  writeln( tab, "<", call message name, ">" )
                            nest = nest + 1
                            call message arguments foreach( arg,
                                                            content := self doMessage(arg)
                                                            if(content type == "Sequence", writeln( tab, content) )
                                                           )
                            nest = nest - 1
                            writeln( tab, "</", call message name, ">" )
                          )


Builder building( library("Volpe"), stadium("Tucker") )



