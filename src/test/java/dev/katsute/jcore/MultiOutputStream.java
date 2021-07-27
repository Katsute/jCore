package dev.katsute.jcore;

import java.io.IOException;
import java.io.OutputStream;

public final class MultiOutputStream extends OutputStream {

    private final OutputStream[] outputStreams;

    public MultiOutputStream(final OutputStream... streams){
        this.outputStreams = streams;
    }

    @Override
    public final void write(final int b) throws IOException{
        for(final OutputStream out : outputStreams)
            out.write(b);
    }

    @Override
    public final void write(final byte[] b) throws IOException{
        for(final OutputStream out : outputStreams)
            out.write(b);
    }

    @Override
    public final void write(final byte[] b, final int off, final int len) throws IOException{
        for(final OutputStream out : outputStreams)
            out.write(b, off, len);
    }

    @Override
    public final void flush() throws IOException{
        for(final OutputStream out : outputStreams)
            out.flush();
    }

    @Override
    public final void close() throws IOException{
        for(final OutputStream out : outputStreams)
            out.close();
    }

}
