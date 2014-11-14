package com.xing.http.connector;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class SocketInputStream extends InputStream {

    // -------------------------------------------------------------- Constants


    /**
     * CR.
     */
    private static final byte CR = (byte) '\r';


    /**
     * LF.
     */
    private static final byte LF = (byte) '\n';


    /**
     * SP.
     */
    private static final byte SP = (byte) ' ';


    /**
     * HT.
     */
    private static final byte HT = (byte) '\t';


    /**
     * COLON.
     */
    private static final byte COLON = (byte) ':';


    /**
     * Lower case offset.
     */
    private static final int LC_OFFSET = 'A' - 'a';


    /**
     * Internal buffer.
     */
    protected byte buf[];


    /**
     * Last valid byte.
     */
    protected int count;


    /**
     * Position in the buffer.缓存位置指标
     */
    protected int pos;


    /**
     * Underlying input stream.
     */
    protected InputStream is;


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a servlet input stream associated with the specified socket
     * input.
     *
     * @param is socket input stream
     * @param bufferSize size of the internal buffer
     */
    public SocketInputStream(InputStream is, int bufferSize) {

        this.is = is;
        buf = new byte[bufferSize];

    }


    // -------------------------------------------------------------- Variables


    /**
     * The string manager for this package.
     */
    //protected static StringManager sm =StringManager.getManager(Constants.Package);


    // ----------------------------------------------------- Instance Variables


    // --------------------------------------------------------- Public Methods


    /**
     * Read the request line, and copies it to the given buffer. This
     * function is meant to be used during the HTTP request header parsing.
     * Do NOT attempt to read the request body using it.
     * 
     * 从HTTP协议的请求字符流中读取请求行内容，并复制到给定的数据结构（HTTPRequestLine）中，用于HTTP协议头部信息的解析，不能用于HTTP协议Body内容
     *
     * @param requestLine Request line object
     * @throws IOException If an exception occurs during the underlying socket
     * read operations, or if the given buffer is not big enough to accomodate
     * the whole line.
     */
    public void readRequestLine(HttpRequestLine requestLine)
        throws IOException {

        // 回收检查
        if (requestLine.methodEnd != 0)
            requestLine.recycle();

        // Checking for a blank line，检查空白行
        int chr = 0;
        do { // Skipping CR or LF
            try {
                chr = read();
            } catch (IOException e) {
                chr = -1;
            }
        } while ((chr == CR) || (chr == LF));
        if (chr == -1){
            //throw new EOFException(sm.getString("requestStream.readline.error"));
        	throw new EOFException("requestStream.readline.error");
        }
        pos--;

        // 读取方法名，从第一个字符开始直到读到空白字符为止，得到的字符串就是方法（GET、POST等等）

        int maxRead = requestLine.method.length;
        @SuppressWarnings("unused")
		int readStart = pos;
        int readCount = 0;

        boolean space = false;

        while (!space) {
            // if the buffer is full, extend it，若存放方法字符串的空间太小则扩大到原来的两倍
            if (readCount >= maxRead) {
                if ((2 * maxRead) <= HttpRequestLine.MAX_METHOD_SIZE) {
                    char[] newBuffer = new char[2 * maxRead];
                    System.arraycopy(requestLine.method, 0, newBuffer, 0,
                                     maxRead);
                    requestLine.method = newBuffer;
                    maxRead = requestLine.method.length;
                } else {
                    //throw new IOException(sm.getString("requestStream.readline.toolong"));
                	throw new IOException("requestStream.readline.toolong");
                }
            }
            // We're at the end of the internal buffer，在还没有完成方法读取的时候也就是还未遇到空白字符的时候，请求内容所有字符已经读取到头，说明
            // HTTP请求内容有问题
            if (pos >= count) {
                int val = read();
                if (val == -1) {
                    //throw new IOException(sm.getString("requestStream.readline.error"));
                	throw new IOException("requestStream.readline.error");
                }
                pos = 0;
                readStart = 0;
            }
            if (buf[pos] == SP) {
                space = true;
            }
            requestLine.method[readCount] = (char) buf[pos];
            readCount++;
            pos++;
        }

        requestLine.methodEnd = readCount - 1;//记录方法字符串的结束位置，方便读取

        // Reading URI，读取URI部分，读取的规则与读取方法的规则类似

        maxRead = requestLine.uri.length;
        readStart = pos;
        readCount = 0;

        space = false;

        boolean eol = false;//本次读取是否完全结束

        while (!space) {
            // if the buffer is full, extend it
            if (readCount >= maxRead) {
                if ((2 * maxRead) <= HttpRequestLine.MAX_URI_SIZE) {
                    char[] newBuffer = new char[2 * maxRead];
                    System.arraycopy(requestLine.uri, 0, newBuffer, 0,
                                     maxRead);
                    requestLine.uri = newBuffer;
                    maxRead = requestLine.uri.length;
                } else {
                    //throw new IOException(sm.getString("requestStream.readline.toolong"));
                	throw new IOException("requestStream.readline.toolong");
                }
            }
            // We're at the end of the internal buffer
            if (pos >= count) {
                int val = read();
                if (val == -1){
                    //throw new IOException(sm.getString("requestStream.readline.error"));
                	throw new IOException("requestStream.readline.error");
                }
                pos = 0;
                readStart = 0;
            }
            if (buf[pos] == SP) {//遇到空白行或者回车换行的时候都视为正常结束
                space = true;
            } else if ((buf[pos] == CR) || (buf[pos] == LF)) {
                // HTTP/0.9 style request
                eol = true;		//完全结束，不再进行其他的读取
                space = true;
            }
            requestLine.uri[readCount] = (char) buf[pos];
            readCount++;
            pos++;
        }

        requestLine.uriEnd = readCount - 1;

        // Reading protocol，读取协议类型部分，规则与上面类似，但区别已经较大，主要对结束的判读
        // 在读取URI的时候就可能会完全结束，也即该部分可能没有运行

        maxRead = requestLine.protocol.length;
        readStart = pos;
        readCount = 0;

        while (!eol) {
            // if the buffer is full, extend it
            if (readCount >= maxRead) {
                if ((2 * maxRead) <= HttpRequestLine.MAX_PROTOCOL_SIZE) {
                    char[] newBuffer = new char[2 * maxRead];
                    System.arraycopy(requestLine.protocol, 0, newBuffer, 0,
                                     maxRead);
                    requestLine.protocol = newBuffer;
                    maxRead = requestLine.protocol.length;
                } else {
                    //throw new IOException(sm.getString("requestStream.readline.toolong"));
                	throw new IOException("requestStream.readline.toolong");
                }
            }
            // We're at the end of the internal buffer
            if (pos >= count) {
                // Copying part (or all) of the internal buffer to the line
                // buffer
                int val = read();
                if (val == -1){
                	//throw new IOException(sm.getString("requestStream.readline.error"));
                	throw new IOException("requestStream.readline.error");
                }       
                pos = 0;
                readStart = 0;
            }
            if (buf[pos] == CR) {
                // Skip CR.
            } else if (buf[pos] == LF) {
                eol = true;
            } else {
                requestLine.protocol[readCount] = (char) buf[pos];
                readCount++;
            }
            pos++;
        }

        requestLine.protocolEnd = readCount;
    }


    /**
     * Read a header, and copies it to the given buffer. This
     * function is meant to be used during the HTTP request header parsing.
     * Do NOT attempt to read the request body using it.
     * 从输入流中读取头部信息
     *
     * @param requestLine Request line object
     * @throws IOException If an exception occurs during the underlying socket
     * read operations, or if the given buffer is not big enough to accomodate
     * the whole line.
     */
    public void readHeader(HttpHeader header)
        throws IOException {

        // Recycling check
        if (header.nameEnd != 0)
            header.recycle();

        // Checking for a blank line
        int chr = read();
        if ((chr == CR) || (chr == LF)) { // Skipping CR
            if (chr == CR)
                read(); // Skipping LF
            header.nameEnd = 0;
            header.valueEnd = 0;
            return;
        } else {
            pos--;
        }

        // Reading the header name

        int maxRead = header.name.length;
        @SuppressWarnings("unused")
		int readStart = pos;
        int readCount = 0;

        boolean colon = false;

        while (!colon) {
            // if the buffer is full, extend it
            if (readCount >= maxRead) {
                if ((2 * maxRead) <= HttpHeader.MAX_NAME_SIZE) {
                    char[] newBuffer = new char[2 * maxRead];
                    System.arraycopy(header.name, 0, newBuffer, 0, maxRead);
                    header.name = newBuffer;
                    maxRead = header.name.length;
                } else {
                    //throw new IOException(sm.getString("requestStream.readline.toolong"));
                }
            }
            // We're at the end of the internal buffer
            if (pos >= count) {
                int val = read();
                if (val == -1) {
                    //throw new IOException(sm.getString("requestStream.readline.error"));
                }
                pos = 0;
                readStart = 0;
            }
            if (buf[pos] == COLON) {
                colon = true;
            }
            char val = (char) buf[pos];
            if ((val >= 'A') && (val <= 'Z')) {
                val = (char) (val - LC_OFFSET);
            }
            header.name[readCount] = val;
            readCount++;
            pos++;
        }

        header.nameEnd = readCount - 1;

        // Reading the header value (which can be spanned over multiple lines)

        maxRead = header.value.length;
        readStart = pos;
        readCount = 0;

        @SuppressWarnings("unused")
		int crPos = -2;

        boolean eol = false;
        boolean validLine = true;

        while (validLine) {

            boolean space = true;

            // Skipping spaces
            // Note : Only leading white spaces are removed. Trailing white
            // spaces are not.
            while (space) {
                // We're at the end of the internal buffer
                if (pos >= count) {
                    // Copying part (or all) of the internal buffer to the line
                    // buffer
                    int val = read();
                    if (val == -1){
                        //throw new IOException(sm.getString("requestStream.readline.error"));
                    }
                    pos = 0;
                    readStart = 0;
                }
                if ((buf[pos] == SP) || (buf[pos] == HT)) {
                    pos++;
                } else {
                    space = false;
                }
            }

            while (!eol) {
                // if the buffer is full, extend it
                if (readCount >= maxRead) {
                    if ((2 * maxRead) <= HttpHeader.MAX_VALUE_SIZE) {
                        char[] newBuffer = new char[2 * maxRead];
                        System.arraycopy(header.value, 0, newBuffer, 0,
                                         maxRead);
                        header.value = newBuffer;
                        maxRead = header.value.length;
                    } else {
                        //throw new IOException(sm.getString("requestStream.readline.toolong"));
                    }
                }
                // We're at the end of the internal buffer
                if (pos >= count) {
                    // Copying part (or all) of the internal buffer to the line
                    // buffer
                    int val = read();
                    if (val == -1){
                        //throw new IOException(sm.getString("requestStream.readline.error"));
                    }
                    pos = 0;
                    readStart = 0;
                }
                if (buf[pos] == CR) {
                } else if (buf[pos] == LF) {
                    eol = true;
                } else {
                    // FIXME : Check if binary conversion is working fine
                    int ch = buf[pos] & 0xff;
                    header.value[readCount] = (char) ch;
                    readCount++;
                }
                pos++;
            }

            int nextChr = read();

            if ((nextChr != SP) && (nextChr != HT)) {
                pos--;
                validLine = false;
            } else {
                eol = false;
                // if the buffer is full, extend it
                if (readCount >= maxRead) {
                    if ((2 * maxRead) <= HttpHeader.MAX_VALUE_SIZE) {
                        char[] newBuffer = new char[2 * maxRead];
                        System.arraycopy(header.value, 0, newBuffer, 0,
                                         maxRead);
                        header.value = newBuffer;
                        maxRead = header.value.length;
                    } else {
                        //throw new IOException(sm.getString("requestStream.readline.toolong"));
                    }
                }
                header.value[readCount] = ' ';
                readCount++;
            }

        }

        header.valueEnd = readCount;

    }


    /**
     * Read byte.
     */
    public int read() throws IOException {
        if (pos >= count) {
            fill();
            if (pos >= count){
                return -1;
            }
        }
        return buf[pos++] & 0xff;
    }


    /**
     *
     */
    /*
    public int read(byte b[], int off, int len)
        throws IOException {

    }
    */


    /**
     *
     */
    /*
    public long skip(long n)
        throws IOException {

    }
    */


    /**
     * Returns the number of bytes that can be read from this input
     * stream without blocking.
     */
    public int available()
        throws IOException {
        return (count - pos) + is.available();
    }


    /**
     * Close the input stream.
     */
    public void close()
        throws IOException {
        if (is == null)
            return;
        is.close();
        is = null;
        buf = null;
    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Fill the internal buffer using data from the undelying input stream.
     */
    protected void fill() throws IOException {
        pos = 0;
        count = 0;
        int nRead = is.read(buf, 0, buf.length);
        if (nRead > 0) {
            count = nRead;
        }
    }

    public InputStream getInputStream(){
    	return this.is;
    }

}
