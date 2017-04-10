# ADS-Huffman
Data compression using Huffman coding and binary, 4-ary, pairing heaps

# Compiling Instructions

To compile the program in Linux environments you can run the below command:

$ make

The above command will generate the class files.
To clear out the class files you can run the below command:

$ make clean

The encoder can be executed using:

$ java encoder <input_file_name>

Eg: java encoded sample_input.txt

This will generate two files: encoded.bin and code_table.txt

The decoder can be executed using:

$ java decoder <encoded_file_name> <code_table_file_name>

Eg: java decoder encoded.bin code_table.txt

This will generate the output file decoded.txt
