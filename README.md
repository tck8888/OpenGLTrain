# OpenGLTrain

## VBO（Vertex Buffer object） 顶点缓冲

    不使用VBO时，我们每次绘制（ glDrawArrays ）图形时都是从本地内存处获取顶点数据然后传输给OpenGL来绘制，
    这样就会频繁的操作CPU->GPU增大开销，从而降低效率。
    使用VBO，我们就能把顶点数据缓存到GPU开辟的一段内存中，然后使用时不必再从本地获取，而是直接从显存中获取，这样就能提升绘制的效率。
