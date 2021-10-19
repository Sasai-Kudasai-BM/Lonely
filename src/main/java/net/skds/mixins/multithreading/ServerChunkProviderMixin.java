package net.skds.mixins.multithreading;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.mojang.datafixers.util.Either;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.util.Util;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.skds.core.api.IServerChunkProvider;
import net.skds.core.api.multithreading.ISKDSThread;

@Mixin(value = { ServerChunkProvider.class })
public abstract class ServerChunkProviderMixin implements IServerChunkProvider {

	@Final
	@Shadow
	public ServerWorld world;
	@Final
	@Shadow
	private Thread mainThread;

	public IChunk getCustomChunk(long l) {
		ChunkHolder chunkHolder = this.func_217213_a(l);
		if (chunkHolder == null) {
			return null;
		}
		return chunkHolder.func_219287_e();
	}

	@Shadow
	private ChunkHolder func_217213_a(long l) {
		return null;
	}

	// @Redirect(method = "getChunk", at = @At(value = "INVOKE", ordinal = 0, target
	// = "Ljava/lang/Thread;currentThread()Ljava/lang/Thread;"))
	// public Thread aaa(int x, int z, ChunkStatus status, boolean b) {
	// return mainThread;
	// }

	@Inject(method = "getChunk", at = @At(value = "HEAD", ordinal = 0), cancellable = true)
	public void getChunk(int chunkX, int chunkZ, ChunkStatus requiredStatus, boolean load,
			CallbackInfoReturnable<IChunk> ci) {
		if (Thread.currentThread() instanceof ISKDSThread) {
			long p = ChunkPos.asLong(chunkX, chunkZ);
			IChunk iChunk = getCustomChunk(p);
			if (load && (iChunk == null || !(iChunk instanceof Chunk))) {
				synchronized (this) {
					CompletableFuture<Either<IChunk, ChunkHolder.IChunkLoadingError>> completablefuture = this
							.func_217233_c(chunkX, chunkZ, requiredStatus, load);
					// this.executor.driveUntil(completablefuture::isDone);
					try {
						iChunk = completablefuture.get().map((p_222874_0_) -> {
							return p_222874_0_;
						}, (p_222870_1_) -> {
							if (load) {
								throw (IllegalStateException) Util.pauseDevMode(
										new IllegalStateException("Chunk not there when requested: " + p_222870_1_));
							} else {
								return null;
							}
						});
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
			}
			ci.setReturnValue(iChunk);
		}
	}

	@Shadow
	private CompletableFuture<Either<IChunk, ChunkHolder.IChunkLoadingError>> func_217233_c(int chunkX, int chunkZ,
			ChunkStatus requiredStatus, boolean load) {
		return null;
	}

}