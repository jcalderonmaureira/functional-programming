package com.paradigmas.fp.monadas;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * MAYBE MONAD
 * ============
 * Envuelve un valor que puede ser null (Nothing) o tener valor (Just).
 * Elimina NullPointerExceptions con un tipo explícito.
 *
 * Java 26: sealed classes + records para representar los dos casos.
 */
public sealed interface Maybe<A> permits Maybe.Just, Maybe.Nothing {

    // ── Constructores ─────────────────────────────────────────────────
    static <A> Maybe<A> of(A value) {
        return value == null ? nothing() : just(value);
    }

    static <A> Maybe<A> just(A value) {
        if (value == null) throw new NullPointerException("Just no acepta null, usa nothing()");
        return new Just<>(value);
    }

    @SuppressWarnings("unchecked")
    static <A> Maybe<A> nothing() {
        return (Nothing<A>) Nothing.INSTANCE;
    }

    // ── Operaciones ───────────────────────────────────────────────────
    <B> Maybe<B> map(Function<A, B> f);
    <B> Maybe<B> flatMap(Function<A, Maybe<B>> f);
    A getOrElse(A defaultValue);
    A getOrElse(Supplier<A> supplier);
    boolean isPresent();
    boolean isEmpty();

    // ── Just: tiene valor ─────────────────────────────────────────────
    record Just<A>(A value) implements Maybe<A> {
        public <B> Maybe<B> map(Function<A, B> f)          { return Maybe.of(f.apply(value)); }
        public <B> Maybe<B> flatMap(Function<A, Maybe<B>> f){ return f.apply(value); }
        public A getOrElse(A defaultValue)                  { return value; }
        public A getOrElse(Supplier<A> supplier)            { return value; }
        public boolean isPresent() { return true; }
        public boolean isEmpty()   { return false; }
        public String toString()   { return "Just(" + value + ")"; }
    }

    // ── Nothing: vacío ────────────────────────────────────────────────
    final class Nothing<A> implements Maybe<A> {
        @SuppressWarnings("rawtypes")
        static final Nothing INSTANCE = new Nothing<>();
        private Nothing() {}
        public <B> Maybe<B> map(Function<A, B> f)           { return nothing(); }
        public <B> Maybe<B> flatMap(Function<A, Maybe<B>> f){ return nothing(); }
        public A getOrElse(A defaultValue)                   { return defaultValue; }
        public A getOrElse(Supplier<A> supplier)             { return supplier.get(); }
        public boolean isPresent() { return false; }
        public boolean isEmpty()   { return true; }
        public String toString()   { return "Nothing"; }
    }
}
