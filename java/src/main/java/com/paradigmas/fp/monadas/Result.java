package com.paradigmas.fp.monadas;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * RESULT MONAD (Either)
 * ======================
 * Ok(valor) o Err(mensaje): manejo de errores sin excepciones.
 * Encadena operaciones que pueden fallar de forma explícita y segura.
 *
 * Java 26: sealed classes + records
 */
public sealed interface Result<A> permits Result.Ok, Result.Err {

    // ── Constructores ─────────────────────────────────────────────────
    static <A> Result<A> ok(A value)      { return new Ok<>(value); }
    static <A> Result<A> err(String error){ return new Err<>(error); }

    static <A> Result<A> attempt(Supplier<A> supplier) {
        try {
            return ok(supplier.get());
        } catch (Exception e) {
            return err(e.getMessage());
        }
    }

    // ── Operaciones ───────────────────────────────────────────────────
    <B> Result<B> map(Function<A, B> f);
    <B> Result<B> flatMap(Function<A, Result<B>> f);
    A getOrElse(A defaultValue);
    boolean isOk();
    boolean isErr();
    String errorOrNull();

    // ── Ok ────────────────────────────────────────────────────────────
    record Ok<A>(A value) implements Result<A> {
        public <B> Result<B> map(Function<A, B> f) {
            return Result.attempt(() -> f.apply(value));
        }
        public <B> Result<B> flatMap(Function<A, Result<B>> f) { return f.apply(value); }
        public A getOrElse(A defaultValue) { return value; }
        public boolean isOk()  { return true; }
        public boolean isErr() { return false; }
        public String errorOrNull() { return null; }
        public String toString() { return "Ok(" + value + ")"; }
    }

    // ── Err ───────────────────────────────────────────────────────────
    record Err<A>(String error) implements Result<A> {
        @SuppressWarnings("unchecked")
        public <B> Result<B> map(Function<A, B> f)           { return (Result<B>) this; }
        @SuppressWarnings("unchecked")
        public <B> Result<B> flatMap(Function<A, Result<B>> f){ return (Result<B>) this; }
        public A getOrElse(A defaultValue) { return defaultValue; }
        public boolean isOk()  { return false; }
        public boolean isErr() { return true; }
        public String errorOrNull() { return error; }
        public String toString() { return "Err(" + error + ")"; }
    }
}
