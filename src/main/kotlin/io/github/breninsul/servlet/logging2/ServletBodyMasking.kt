/*
 * MIT License
 *
 * Copyright (c) 2024 BreninSul
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.breninsul.servlet.logging2

import io.github.breninsul.logging2.*

interface ServletUriMasking : HttpUriMasking

interface ServletRequestBodyMasking : HttpRequestBodyMasking

interface ServletResponseBodyMasking : HttpResponseBodyMasking

open class ServletRequestBodyMaskingDelegate(
    protected open val delegate: HttpBodyMasking,
) : ServletRequestBodyMasking {
    override fun mask(message: String?): String = delegate.mask(message)
    override fun type(): HttpBodyType = delegate.type()
}

open class ServletResponseBodyMaskingDelegate(
    protected open val delegate: HttpBodyMasking,
) : ServletResponseBodyMasking {
    override fun mask(message: String?): String = delegate.mask(message)
    override fun type(): HttpBodyType = delegate.type()
}

open class ServletUriMaskingDelegate(
    protected open val delegate: HttpUriMasking,
) : ServletUriMasking {
    override fun mask(uri: String?): String = delegate.mask(uri)
}
