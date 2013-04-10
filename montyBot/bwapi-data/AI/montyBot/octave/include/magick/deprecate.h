/*
  Copyright (C) 2003, 2008 GraphicsMagick Group
  Copyright (C) 2002 ImageMagick Studio
 
  This program is covered by multiple licenses, which are described in
  Copyright.txt. You should have received a copy of Copyright.txt with this
  package; otherwise see http://www.graphicsmagick.org/www/Copyright.html.
 
  Log methods.
*/
#ifndef _MAGICK_DEPRECATE_H
#define _MAGICK_DEPRECATE_H

#if defined(__cplusplus) || defined(c_plusplus)
extern "C" {
#endif

  /*
    Legacy names for (possibly) large integral types
  */

#if !defined(ExtendedSignedIntegralType)
#  define ExtendedSignedIntegralType magick_int64_t
#endif
#if !defined(ExtendedUnsignedIntegralType)
#  define ExtendedUnsignedIntegralType magick_uint64_t
#endif

  /*
    Compatibility definitions to handle the renaming of
    ExtendedSignedIntegralType and ExtendedUnsignedIntegralType to
    MagickSignedType and MagickUnsignedType which occured in ImageMagick
    5.5.8.  ImageMagick 5.5.8 also introduced MagickRationalType.
  */
#if !defined(MagickSignedType)
#  define MagickSignedType magick_int64_t
#endif
#if !defined(MagickUnsignedType)
#  define MagickUnsignedType magick_uint64_t
#endif
#if !defined(MagickRationalType)
#  if defined(HAVE_LONG_DOUBLE)
#    define MagickRationalType long double
#  else
#    define MagickRationalType double
#  endif
#endif

  extern MagickExport unsigned int
  PopImagePixels(const Image *,const QuantumType,unsigned char *) __attribute__ ((deprecated));

  extern MagickExport unsigned int
  PushImagePixels(Image *,const QuantumType,const unsigned char *) __attribute__ ((deprecated));

  extern MagickExport void
  *AcquireMemory(const size_t) __attribute__ ((deprecated));

  extern MagickExport void
  *CloneMemory(void *,const void *,const size_t) __attribute__ ((deprecated));

  extern MagickExport void
  LiberateMemory(void **) __attribute__ ((deprecated));

  extern MagickExport void
  ReacquireMemory(void **,const size_t) __attribute__ ((deprecated));

  extern MagickExport const PixelPacket
  *AcquireCacheView(const ViewInfo *view,
                    const long x,const long y,const unsigned long columns,
                    const unsigned long rows,ExceptionInfo *exception) __attribute__ ((deprecated));

  extern MagickExport PixelPacket
  *GetCacheView(ViewInfo *view,const long x,const long y,
                const unsigned long columns,const unsigned long rows)  __attribute__ ((deprecated));

  extern MagickExport PixelPacket
  *SetCacheView(ViewInfo *view,const long x,const long y,
                const unsigned long columns,const unsigned long rows) __attribute__ ((deprecated));

  extern MagickExport MagickPassFail
  SyncCacheView(ViewInfo *view) __attribute__ ((deprecated));

#if defined(__cplusplus) || defined(c_plusplus)
}
#endif

#endif
