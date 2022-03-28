package hello.batch.springbatch.listener;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class CustomChunkListener implements ChunkListener {
	@Override
	public void beforeChunk(ChunkContext chunkContext) {
		System.out.println(".beforeChunk");
	}

	@Override
	public void afterChunk(ChunkContext chunkContext) {
		System.out.println(".afterChunk");
	}

	@Override
	public void afterChunkError(ChunkContext chunkContext) {
		System.out.println(".afterChunkError");
	}
}
