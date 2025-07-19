package io.github.leaderman.makemoney.hustle.limiter;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import io.lettuce.core.codec.RedisCodec;

public class StringByteArrayCodec implements RedisCodec<String, byte[]> {
  @Override
  public ByteBuffer encodeKey(String key) {
    return StandardCharsets.UTF_8.encode(key);
  }

  @Override
  public ByteBuffer encodeValue(byte[] value) {
    return ByteBuffer.wrap(value);
  }

  @Override
  public String decodeKey(ByteBuffer buffer) {
    return StandardCharsets.UTF_8.decode(buffer).toString();
  }

  @Override
  public byte[] decodeValue(ByteBuffer buffer) {
    byte[] result = new byte[buffer.remaining()];
    buffer.get(result);

    return result;
  }
}
