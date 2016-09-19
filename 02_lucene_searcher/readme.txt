Field.Store.YES或者NO(存储域选项)
设置为YES表示或把这个域中的内容完全存储到文件中，方便进行文本的还原
设置为NO表示把这个域的内容不存储到文件中，但是可以被索引，此时内容无法完全还原(doc.get)

Field.Index(索引选项)
Index.ANALYZED:进行分词和索引，适用于标题、内容等
Index.NOT_ANALYZED:进行索引，但是不进行分词，如果身份证号，姓名，ID等，适用于精确搜索
Index.ANALYZED_NOT_NORMS:进行分词但是不存储norms信息，这个norms中包括了创建索引的时间和权值等信息
Index.NOT_ANALYZED_NOT_NORMS:即不进行分词也不存储norms信息
Index.NO:不进行索引 