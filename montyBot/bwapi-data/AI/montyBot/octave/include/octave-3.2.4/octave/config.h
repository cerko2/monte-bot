/* config.h.  Generated from config.h.in by configure.  */
/* config.h.in.  Generated from configure.in by autoheader.  */

/* Define to use internal bounds checking. */
/* #undef BOUNDS_CHECKING */

/* Define to 1 if the `closedir' function returns void instead of `int'. */
/* #undef CLOSEDIR_VOID */

/* Define to one of `_getb67', `GETB67', `getb67' for Cray-2 and Cray-YMP
   systems. This function is required for `alloca.c' support on those systems.
   */
/* #undef CRAY_STACKSEG_END */

/* Define if C++ reinterpret_cast fails for function pointers. */
/* #undef CXX_BROKEN_REINTERPRET_CAST */

/* Define if your C++ runtime library is ISO compliant. */
#define CXX_ISO_COMPLIANT_LIBRARY 1

/* Define if your compiler supports `<>' stuff for template friends. */
#define CXX_NEW_FRIEND_TEMPLATE_DECL 1

/* Define to 1 if using `alloca.c'. */
/* #undef C_ALLOCA */

/* Define if using dynamic linking */
#define ENABLE_DYNAMIC_LINKING 1

/* Define if your math.h declares struct exception for matherr(). */
/* #undef EXCEPTION_IN_MATH */

/* Define to dummy `main' function (if any) required to link to the Fortran
   libraries. */
/* #undef F77_DUMMY_MAIN */

/* Define to a macro mangling the given C identifier (in lower and upper
   case), which must not contain underscores, for linking with Fortran. */
#define F77_FUNC(name,NAME) name ## _

/* As F77_FUNC, but for C identifiers containing underscores. */
#define F77_FUNC_(name,NAME) name ## _

/* Define if F77 and FC dummy `main' functions are identical. */
/* #undef FC_DUMMY_MAIN_EQ_F77 */

/* Define if your system has a single-arg prototype for gettimeofday. */
/* #undef GETTIMEOFDAY_NO_TZ */

/* Define if GLPK version is less than 4.14. */
/* #undef GLPK_PRE_4_14 */

/* Define to 1 if you have the `acosh' function. */
#define HAVE_ACOSH 1

/* Define to 1 if you have the `acoshf' function. */
#define HAVE_ACOSHF 1

/* Define to 1 if you have `alloca', as a function or macro. */
#define HAVE_ALLOCA 1

/* Define to 1 if you have <alloca.h> and it should be used (not on Ultrix).
   */
/* #undef HAVE_ALLOCA_H */

/* Define if the AMD library is used. */
#define HAVE_AMD 1

/* Define to 1 if you have the <amd/amd.h> header file. */
/* #undef HAVE_AMD_AMD_H */

/* Define to 1 if you have the <amd.h> header file. */
/* #undef HAVE_AMD_H */

/* Define if the ARPACK library is used. */
#define HAVE_ARPACK 1

/* Define to 1 if you have the `asinh' function. */
#define HAVE_ASINH 1

/* Define to 1 if you have the `asinhf' function. */
#define HAVE_ASINHF 1

/* Define to 1 if you have the <assert.h> header file. */
#define HAVE_ASSERT_H 1

/* Define to 1 if you have the `atanh' function. */
#define HAVE_ATANH 1

/* Define to 1 if you have the `atanhf' function. */
#define HAVE_ATANHF 1

/* Define to 1 if you have the `atexit' function. */
#define HAVE_ATEXIT 1

/* Define to 1 if you have the `basename' function. */
#define HAVE_BASENAME 1

/* Define to 1 if you have the `bcopy' function. */
/* #undef HAVE_BCOPY */

/* Define if you have a BLAS library. */
#define HAVE_BLAS 1

/* Define if you have BSD style signals. */
/* #undef HAVE_BSD_SIGNALS */

/* Define to 1 if you have the `bzero' function. */
/* #undef HAVE_BZERO */

/* Define if you have a c99 vsnprintf */
#define HAVE_C99_VSNPRINTF 1

/* Define to 1 if you have the `canonicalize_file_name' function. */
/* #undef HAVE_CANONICALIZE_FILE_NAME */

/* Define if the CCOLAMD library is used. */
#define HAVE_CCOLAMD 1

/* Define to 1 if you have the <ccolamd/ccolamd.h> header file. */
/* #undef HAVE_CCOLAMD_CCOLAMD_H */

/* Define to 1 if you have the <ccolamd.h> header file. */
/* #undef HAVE_CCOLAMD_H */

/* Define to 1 if you have the `chmod' function. */
#define HAVE_CHMOD 1

/* Define if the CHOLMOD library is used. */
#define HAVE_CHOLMOD 1

/* Define to 1 if you have the <cholmod/cholmod.h> header file. */
/* #undef HAVE_CHOLMOD_CHOLMOD_H */

/* Define to 1 if you have the <cholmod.h> header file. */
/* #undef HAVE_CHOLMOD_H */

/* Define if <cmath> provides isfinite */
#define HAVE_CMATH_ISFINITE 1

/* Define if <cmath> provides float variant of isfinite */
#define HAVE_CMATH_ISFINITEF 1

/* Define if <cmath> provides isinf */
#define HAVE_CMATH_ISINF 1

/* Define if <cmath> provides float variant of isinf */
#define HAVE_CMATH_ISINFF 1

/* Define if <cmath> provides isnan */
#define HAVE_CMATH_ISNAN 1

/* Define if <cmath> provides float variant of isnan */
#define HAVE_CMATH_ISNANF 1

/* Define if the COLAMD library is used. */
#define HAVE_COLAMD 1

/* Define to 1 if you have the <colamd/colamd.h> header file. */
/* #undef HAVE_COLAMD_COLAMD_H */

/* Define to 1 if you have the <colamd.h> header file. */
/* #undef HAVE_COLAMD_H */

/* Define to 1 if you have the <conio.h> header file. */
#define HAVE_CONIO_H 1

/* Define to 1 if you have the `copysign' function. */
#define HAVE_COPYSIGN 1

/* Define to 1 if you have the <cs.h> header file. */
/* #undef HAVE_CS_H */

/* Define if CURL is available. */
#define HAVE_CURL 1

/* Define to 1 if you have the <curl/curl.h> header file. */
#define HAVE_CURL_CURL_H 1

/* Define to 1 if you have the <curses.h> header file. */
#define HAVE_CURSES_H 1

/* Define if the CXSparse library is used. */
#define HAVE_CXSPARSE 1

/* Define to 1 if you have the <cxsparse/cs.h> header file. */
/* #undef HAVE_CXSPARSE_CS_H */

/* Define to 1 if you have the declaration of `exp2', and to 0 if you don't.
   */
#define HAVE_DECL_EXP2 1

/* Define to 1 if you have the declaration of `round', and to 0 if you don't.
   */
#define HAVE_DECL_ROUND 1

/* Define to 1 if you have the declaration of `signbit', and to 0 if you
   don't. */
#define HAVE_DECL_SIGNBIT 1

/* Define to 1 if you have the declaration of `sys_siglist', and to 0 if you
   don't. */
#define HAVE_DECL_SYS_SIGLIST 0

/* Define to 1 if you have the declaration of `tgamma', and to 0 if you don't.
   */
#define HAVE_DECL_TGAMMA 1

/* Define to 1 if you have the declaration of `tzname', and to 0 if you don't.
   */
#define HAVE_DECL_TZNAME 1

/* Define to 1 if the system has the type `dev_t'. */
#define HAVE_DEV_T 1

/* Define to 1 if you have the <direct.h> header file. */
#define HAVE_DIRECT_H 1

/* Define to 1 if you have the <dirent.h> header file, and it defines `DIR'.
   */
#define HAVE_DIRENT_H 1

/* Define to 1 if you have the `dlclose' function. */
/* #undef HAVE_DLCLOSE */

/* Define to 1 if you have the `dlerror' function. */
/* #undef HAVE_DLERROR */

/* Define to 1 if you have the <dlfcn.h> header file. */
/* #undef HAVE_DLFCN_H */

/* Define to 1 if you have the `dlopen' function. */
/* #undef HAVE_DLOPEN */

/* Define if your system has dlopen, dlsym, dlerror, and dlclose for dynamic
   linking */
/* #undef HAVE_DLOPEN_API */

/* Define to 1 if you have the `dlsym' function. */
/* #undef HAVE_DLSYM */

/* Define to 1 if you have the `dup2' function. */
#define HAVE_DUP2 1

/* Define if your system has dyld for dynamic linking */
/* #undef HAVE_DYLD_API */

/* Define if C++ supports dynamic auto arrays */
#define HAVE_DYNAMIC_AUTO_ARRAYS 1

/* Define to 1 if you have the `endgrent' function. */
/* #undef HAVE_ENDGRENT */

/* Define to 1 if you have the `endpwent' function. */
/* #undef HAVE_ENDPWENT */

/* Define to 1 if you have the `erf' function. */
#define HAVE_ERF 1

/* Define to 1 if you have the `erfc' function. */
#define HAVE_ERFC 1

/* Define to 1 if you have the `erfcf' function. */
#define HAVE_ERFCF 1

/* Define to 1 if you have the `erff' function. */
#define HAVE_ERFF 1

/* Define to 1 if you have the `execvp' function. */
#define HAVE_EXECVP 1

/* Define to 1 if you have the `exp2' function. */
#define HAVE_EXP2 1

/* Define to 1 if you have the `exp2f' function. */
#define HAVE_EXP2F 1

/* Define to 1 if you have the `expm1' function. */
#define HAVE_EXPM1 1

/* Define to 1 if you have the `expm1f' function. */
#define HAVE_EXPM1F 1

/* Define if signed integers use two's complement */
#define HAVE_FAST_INT_OPS 1

/* Define to 1 if you have the `fcntl' function. */
/* #undef HAVE_FCNTL */

/* Define to 1 if you have the <fcntl.h> header file. */
#define HAVE_FCNTL_H 1

/* Define if the FFTW3 library is used. */
#define HAVE_FFTW3 1

/* Define to 1 if you have the `finite' function. */
#define HAVE_FINITE 1

/* Define to 1 if you have the <floatingpoint.h> header file. */
/* #undef HAVE_FLOATINGPOINT_H */

/* Define to 1 if you have the <float.h> header file. */
#define HAVE_FLOAT_H 1

/* Define if FLTK is available */
#define HAVE_FLTK 1

/* Define to 1 if you have the `fnmatch' function. */
/* #undef HAVE_FNMATCH */

/* Define to 1 if you have the <fnmatch.h> header file. */
#define HAVE_FNMATCH_H 1

/* Define to 1 if you have the `fork' function. */
/* #undef HAVE_FORK */

/* Define if framework CARBON is available. */
/* #undef HAVE_FRAMEWORK_CARBON */

/* Define if framework OPENGL is available. */
/* #undef HAVE_FRAMEWORK_OPENGL */

/* Define to 1 if you have the `fstat' function. */
#define HAVE_FSTAT 1

/* Define to 1 if FTGL is present */
#define HAVE_FTGL 1

/* Define to 1 if you have the <FTGL/FTGL.h> header file. */
#define HAVE_FTGL_FTGL_H 1

/* Define to 1 if you have the <FTGL.h> header file. */
/* #undef HAVE_FTGL_H */

/* Define to 1 if you have FTGL.h or FTGL/FTGL.h */
/* #undef HAVE_FTGL_UPPERCASE */

/* Define to 1 if you have the `getcwd' function. */
#define HAVE_GETCWD 1

/* Define to 1 if you have the `getegid' function. */
/* #undef HAVE_GETEGID */

/* Define to 1 if you have the `geteuid' function. */
/* #undef HAVE_GETEUID */

/* Define to 1 if you have the `getgid' function. */
/* #undef HAVE_GETGID */

/* Define to 1 if you have the `getgrent' function. */
/* #undef HAVE_GETGRENT */

/* Define to 1 if you have the `getgrgid' function. */
/* #undef HAVE_GETGRGID */

/* Define to 1 if you have the `getgrnam' function. */
/* #undef HAVE_GETGRNAM */

/* Define to 1 if you have the `gethostname' function. */
/* #undef HAVE_GETHOSTNAME */

/* Define to 1 if you have the `getpgrp' function. */
/* #undef HAVE_GETPGRP */

/* Define to 1 if you have the `getpid' function. */
#define HAVE_GETPID 1

/* Define to 1 if you have the `getppid' function. */
/* #undef HAVE_GETPPID */

/* Define to 1 if you have the `getpwent' function. */
/* #undef HAVE_GETPWENT */

/* Define to 1 if you have the `getpwnam' function. */
/* #undef HAVE_GETPWNAM */

/* Define to 1 if you have the `getpwuid' function. */
/* #undef HAVE_GETPWUID */

/* Define to 1 if you have the `getrusage' function. */
/* #undef HAVE_GETRUSAGE */

/* Define to 1 if you have the `gettimeofday' function. */
#define HAVE_GETTIMEOFDAY 1

/* Define to 1 if you have the `getuid' function. */
/* #undef HAVE_GETUID */

/* Define to 1 if you have the `getwd' function. */
/* #undef HAVE_GETWD */

/* Define to 1 if you have the `glob' function. */
/* #undef HAVE_GLOB */

/* Define to 1 if you have the <glob.h> header file. */
#define HAVE_GLOB_H 1

/* Define if GLPK is available. */
#define HAVE_GLPK 1

/* Define to 1 if you have the <glpk/glpk.h> header file. */
#define HAVE_GLPK_GLPK_H 1

/* Define to 1 if you have the <glpk.h> header file. */
/* #undef HAVE_GLPK_H */

/* Define if gluTessCallback is called with (...) */
/* #undef HAVE_GLUTESSCALLBACK_THREEDOTS */

/* Define to 1 if you have the <GL/glu.h> header file. */
#define HAVE_GL_GLU_H 1

/* Define to 1 if you have the <GL/gl.h> header file. */
#define HAVE_GL_GL_H 1

/* Define to 1 if you have the <grp.h> header file. */
/* #undef HAVE_GRP_H */

/* Define if HDF5 has H5Gget_num_objs. */
#define HAVE_H5GGET_NUM_OBJS 1

/* Define if HDF5 is available. */
#define HAVE_HDF5 1

/* Define to 1 if you have the <hdf5.h> header file. */
#define HAVE_HDF5_H 1

/* Define to 1 if you have the `hypotf' function. */
#define HAVE_HYPOTF 1

/* Define if your system uses IEEE 754 data format. */
#define HAVE_IEEE754_DATA_FORMAT 1

/* Define to 1 if you have the <ieeefp.h> header file. */
/* #undef HAVE_IEEEFP_H */

/* Define to 1 if the system has the type `ino_t'. */
#define HAVE_INO_T 1

/* Define to 1 if you have the <inttypes.h> header file. */
#define HAVE_INTTYPES_H 1

/* Define to 1 if you have the `isinf' function. */
/* #undef HAVE_ISINF */

/* Define to 1 if you have the `isnan' function. */
#define HAVE_ISNAN 1

/* Define to 1 if you have the `kill' function. */
/* #undef HAVE_KILL */

/* Define if you have LAPACK library. */
/* #undef HAVE_LAPACK */

/* Define to 1 if you have the `lgamma' function. */
#define HAVE_LGAMMA 1

/* Define to 1 if you have the `lgammaf' function. */
#define HAVE_LGAMMAF 1

/* Define to 1 if you have the `lgammaf_r' function. */
/* #undef HAVE_LGAMMAF_R */

/* Define to 1 if you have the `lgamma_r' function. */
/* #undef HAVE_LGAMMA_R */

/* Define to 1 if you have the `dl' library (-ldl). */
/* #undef HAVE_LIBDL */

/* Define to 1 if you have the `dld' library (-ldld). */
/* #undef HAVE_LIBDLD */

/* Define to 1 if you have the `m' library (-lm). */
#define HAVE_LIBM 1

/* Define to 1 if you have the `socket' library (-lsocket). */
/* #undef HAVE_LIBSOCKET */

/* Define to 1 if you have the `sun' library (-lsun). */
/* #undef HAVE_LIBSUN */

/* Define to 1 if you have the `wsock32' library (-lwsock32). */
/* #undef HAVE_LIBWSOCK32 */

/* Define to 1 if you have the <limits.h> header file. */
#define HAVE_LIMITS_H 1

/* Define to 1 if you have the `link' function. */
/* #undef HAVE_LINK */

/* Define to 1 if you have the `LoadLibrary' function. */
/* #undef HAVE_LOADLIBRARY */

/* Define if your system has LoadLibrary for dynamic linking */
#define HAVE_LOADLIBRARY_API 1

/* Define to 1 if you have the <locale.h> header file. */
#define HAVE_LOCALE_H 1

/* Define to 1 if you have the `localtime_r' function. */
/* #undef HAVE_LOCALTIME_R */

/* Define to 1 if you have the `log1p' function. */
#define HAVE_LOG1P 1

/* Define to 1 if you have the `log1pf' function. */
#define HAVE_LOG1PF 1

/* Define to 1 if you have the `log2' function. */
#define HAVE_LOG2 1

/* Define to 1 if you have the `log2f' function. */
#define HAVE_LOG2F 1

/* Define to 1 if the system has the type `long long int'. */
#define HAVE_LONG_LONG_INT 1

/* Define to 1 if you have the `lstat' function. */
/* #undef HAVE_LSTAT */

/* Define if GraphicsMagick++ is available. */
#define HAVE_MAGICK 1

/* Define to 1 if you have the `memmove' function. */
#define HAVE_MEMMOVE 1

/* Define to 1 if you have the <memory.h> header file. */
#define HAVE_MEMORY_H 1

/* Define to 1 if you have the `mkdir' function. */
#define HAVE_MKDIR 1

/* Define to 1 if you have the `mkfifo' function. */
/* #undef HAVE_MKFIFO */

/* Define to 1 if you have the `mkstemp' function. */
/* #undef HAVE_MKSTEMP */

/* Define if mkstemps is available in libiberty. */
#define HAVE_MKSTEMPS 1

/* Define to 1 if you have the <nan.h> header file. */
/* #undef HAVE_NAN_H */

/* Define to 1 if you have the <ncurses.h> header file. */
#define HAVE_NCURSES_H 1

/* Define to 1 if you have the <ndir.h> header file, and it defines `DIR'. */
/* #undef HAVE_NDIR_H */

/* Define to 1 if the system has the type `nlink_t'. */
/* #undef HAVE_NLINK_T */

/* Define to 1 if you have the `on_exit' function. */
/* #undef HAVE_ON_EXIT */

/* Define if OpenGL is available */
#define HAVE_OPENGL 1

/* Define to 1 if you have the <OpenGL/glu.h> header file. */
/* #undef HAVE_OPENGL_GLU_H */

/* Define to 1 if you have the <OpenGL/gl.h> header file. */
/* #undef HAVE_OPENGL_GL_H */

/* Define if PCRE library is available. */
#define HAVE_PCRE 1

/* Define to 1 if you have the `pcre_compile' function. */
#define HAVE_PCRE_COMPILE 1

/* Define to 1 if you have the `pipe' function. */
/* #undef HAVE_PIPE */

/* Define if C++ supports operator delete(void *, void *) */
#define HAVE_PLACEMENT_DELETE 1

/* Define to 1 if you have the `poll' function. */
/* #undef HAVE_POLL */

/* Define to 1 if you have the <poll.h> header file. */
/* #undef HAVE_POLL_H */

/* Define if you have POSIX style signals. */
/* #undef HAVE_POSIX_SIGNALS */

/* Define if you have POSIX threads libraries and header files. */
#define HAVE_PTHREAD 1

/* Define to 1 if you have the <pthread.h> header file. */
#define HAVE_PTHREAD_H 1

/* Define to 1 if you have the `putenv' function. */
#define HAVE_PUTENV 1

/* Define to 1 if you have the <pwd.h> header file. */
/* #undef HAVE_PWD_H */

/* Define if the QHull library is used. */
#define HAVE_QHULL 1

/* Define if the qrupdate library is used. */
#define HAVE_QRUPDATE 1

/* Define to 1 if you have the `raise' function. */
#define HAVE_RAISE 1

/* Define to 1 if you have the `readlink' function. */
/* #undef HAVE_READLINK */

/* Define to 1 if you have the `realpath' function. */
/* #undef HAVE_REALPATH */

/* Define if regex library is available. */
#define HAVE_REGEX 1

/* Define to 1 if you have the `regexec' function. */
/* #undef HAVE_REGEXEC */

/* Define to 1 if you have the `rename' function. */
#define HAVE_RENAME 1

/* Define to 1 if you have the `resolvepath' function. */
/* #undef HAVE_RESOLVEPATH */

/* Define to 1 if you have the `rindex' function. */
/* #undef HAVE_RINDEX */

/* Define to 1 if you have the `rmdir' function. */
#define HAVE_RMDIR 1

/* Define to 1 if you have the `round' function. */
#define HAVE_ROUND 1

/* Define to 1 if you have the `roundl' function. */
#define HAVE_ROUNDL 1

/* Define to 1 if you have the `select' function. */
/* #undef HAVE_SELECT */

/* Define to 1 if you have the `setgrent' function. */
/* #undef HAVE_SETGRENT */

/* Define to 1 if you have the `setlocale' function. */
#define HAVE_SETLOCALE 1

/* Define to 1 if you have the `setpwent' function. */
/* #undef HAVE_SETPWENT */

/* Define to 1 if you have the `setvbuf' function. */
#define HAVE_SETVBUF 1

/* Define to 1 if you have the <sgtty.h> header file. */
/* #undef HAVE_SGTTY_H */

/* Define to 1 if you have the `shl_findsym' function. */
/* #undef HAVE_SHL_FINDSYM */

/* Define to 1 if you have the `shl_load' function. */
/* #undef HAVE_SHL_LOAD */

/* Define if your system has shl_load and shl_findsym for dynamic linking */
/* #undef HAVE_SHL_LOAD_API */

/* Define to 1 if you have the `sigaction' function. */
/* #undef HAVE_SIGACTION */

/* Define to 1 if you have the `siglongjmp' function. */
/* #undef HAVE_SIGLONGJMP */

/* Define to 1 if you have the `signbit' function. */
#define HAVE_SIGNBIT 1

/* Define to 1 if you have the `sigpending' function. */
/* #undef HAVE_SIGPENDING */

/* Define to 1 if you have the `sigprocmask' function. */
/* #undef HAVE_SIGPROCMASK */

/* Define to 1 if the system has the type `sigset_t'. */
#define HAVE_SIGSET_T 1

/* Define to 1 if you have the `sigsuspend' function. */
/* #undef HAVE_SIGSUSPEND */

/* Define to 1 if the system has the type `sig_atomic_t'. */
#define HAVE_SIG_ATOMIC_T 1

/* Define to 1 if you have the `snprintf' function. */
#define HAVE_SNPRINTF 1

/* Define to 1 if you have the <sstream> header file. */
#define HAVE_SSTREAM 1

/* Define to 1 if you have the `stat' function. */
#define HAVE_STAT 1

/* Define to 1 if you have the <stdint.h> header file. */
#define HAVE_STDINT_H 1

/* Define to 1 if you have the <stdlib.h> header file. */
#define HAVE_STDLIB_H 1

/* Define to 1 if you have the `strcasecmp' function. */
#define HAVE_STRCASECMP 1

/* Define to 1 if you have the `strdup' function. */
#define HAVE_STRDUP 1

/* Define to 1 if you have the `strerror' function. */
#define HAVE_STRERROR 1

/* Define to 1 if you have the `strftime' function. */
/* #undef HAVE_STRFTIME */

/* Define to 1 if you have the `stricmp' function. */
#define HAVE_STRICMP 1

/* Define to 1 if you have the <strings.h> header file. */
#define HAVE_STRINGS_H 1

/* Define to 1 if you have the <string.h> header file. */
#define HAVE_STRING_H 1

/* Define to 1 if you have the `strncasecmp' function. */
#define HAVE_STRNCASECMP 1

/* Define to 1 if you have the `strnicmp' function. */
#define HAVE_STRNICMP 1

/* Define to 1 if you have the `strptime' function. */
/* #undef HAVE_STRPTIME */

/* Define to 1 if you have the `strsignal' function. */
/* #undef HAVE_STRSIGNAL */

/* Define to 1 if `gr_passwd' is member of `struct group'. */
/* #undef HAVE_STRUCT_GROUP_GR_PASSWD */

/* Define to 1 if `st_blksize' is member of `struct stat'. */
/* #undef HAVE_STRUCT_STAT_ST_BLKSIZE */

/* Define to 1 if `st_blocks' is member of `struct stat'. */
/* #undef HAVE_STRUCT_STAT_ST_BLOCKS */

/* Define to 1 if `st_rdev' is member of `struct stat'. */
#define HAVE_STRUCT_STAT_ST_RDEV 1

/* Define to 1 if `tm_zone' is member of `struct tm'. */
/* #undef HAVE_STRUCT_TM_TM_ZONE */

/* Define to 1 if you have the <suitesparse/amd.h> header file. */
#define HAVE_SUITESPARSE_AMD_H 1

/* Define to 1 if you have the <suitesparse/ccolamd.h> header file. */
#define HAVE_SUITESPARSE_CCOLAMD_H 1

/* Define to 1 if you have the <suitesparse/cholmod.h> header file. */
#define HAVE_SUITESPARSE_CHOLMOD_H 1

/* Define to 1 if you have the <suitesparse/colamd.h> header file. */
#define HAVE_SUITESPARSE_COLAMD_H 1

/* Define to 1 if you have the <suitesparse/cs.h> header file. */
#define HAVE_SUITESPARSE_CS_H 1

/* Define to 1 if you have the <suitesparse/umfpack.h> header file. */
#define HAVE_SUITESPARSE_UMFPACK_H 1

/* Define to 1 if you have the <sunmath.h> header file. */
/* #undef HAVE_SUNMATH_H */

/* Define to 1 if you have the `symlink' function. */
/* #undef HAVE_SYMLINK */

/* Define to 1 if you have the <sys/dir.h> header file, and it defines `DIR'.
   */
/* #undef HAVE_SYS_DIR_H */

/* Define to 1 if you have the <sys/ioctl.h> header file. */
/* #undef HAVE_SYS_IOCTL_H */

/* Define to 1 if you have the <sys/ndir.h> header file, and it defines `DIR'.
   */
/* #undef HAVE_SYS_NDIR_H */

/* Define to 1 if you have the <sys/param.h> header file. */
#define HAVE_SYS_PARAM_H 1

/* Define to 1 if you have the <sys/poll.h> header file. */
/* #undef HAVE_SYS_POLL_H */

/* Define to 1 if you have the <sys/resource.h> header file. */
/* #undef HAVE_SYS_RESOURCE_H */

/* Define to 1 if you have the <sys/select.h> header file. */
/* #undef HAVE_SYS_SELECT_H */

/* Define to 1 if you have the <sys/stat.h> header file. */
#define HAVE_SYS_STAT_H 1

/* Define to 1 if you have the <sys/times.h> header file. */
/* #undef HAVE_SYS_TIMES_H */

/* Define to 1 if you have the <sys/time.h> header file. */
#define HAVE_SYS_TIME_H 1

/* Define to 1 if you have the <sys/types.h> header file. */
#define HAVE_SYS_TYPES_H 1

/* Define to 1 if you have the <sys/utime.h> header file. */
#define HAVE_SYS_UTIME_H 1

/* Define to 1 if you have the <sys/utsname.h> header file. */
/* #undef HAVE_SYS_UTSNAME_H */

/* Define to 1 if you have <sys/wait.h> that is POSIX.1 compatible. */
/* #undef HAVE_SYS_WAIT_H */

/* Define to 1 if you have the `tempnam' function. */
#define HAVE_TEMPNAM 1

/* Define to 1 if you have the <termcap.h> header file. */
#define HAVE_TERMCAP_H 1

/* Define to 1 if you have the <termios.h> header file. */
/* #undef HAVE_TERMIOS_H */

/* Define to 1 if you have the <termio.h> header file. */
/* #undef HAVE_TERMIO_H */

/* Define to 1 if you have the `tgamma' function. */
#define HAVE_TGAMMA 1

/* Define to 1 if you have the `tgammaf' function. */
#define HAVE_TGAMMAF 1

/* Define to 1 if you have the `times' function. */
/* #undef HAVE_TIMES */

/* Define if struct timeval is defined. */
#define HAVE_TIMEVAL 1

/* Define to 1 if your `struct tm' has `tm_zone'. Deprecated, use
   `HAVE_STRUCT_TM_TM_ZONE' instead. */
/* #undef HAVE_TM_ZONE */

/* Define to 1 if you have the `trunc' function. */
#define HAVE_TRUNC 1

/* Define to 1 if you don't have `tm_zone' but do have the external array
   `tzname'. */
#define HAVE_TZNAME 1

/* Define to 1 if you have the <ufsparse/amd.h> header file. */
/* #undef HAVE_UFSPARSE_AMD_H */

/* Define to 1 if you have the <ufsparse/ccolamd.h> header file. */
/* #undef HAVE_UFSPARSE_CCOLAMD_H */

/* Define to 1 if you have the <ufsparse/cholmod.h> header file. */
/* #undef HAVE_UFSPARSE_CHOLMOD_H */

/* Define to 1 if you have the <ufsparse/colamd.h> header file. */
/* #undef HAVE_UFSPARSE_COLAMD_H */

/* Define to 1 if you have the <ufsparse/cs.h> header file. */
/* #undef HAVE_UFSPARSE_CS_H */

/* Define to 1 if you have the <ufsparse/umfpack.h> header file. */
/* #undef HAVE_UFSPARSE_UMFPACK_H */

/* Define to 1 if you have the `umask' function. */
#define HAVE_UMASK 1

/* Define if the UMFPACK library is used. */
#define HAVE_UMFPACK 1

/* Define to 1 if you have the <umfpack.h> header file. */
/* #undef HAVE_UMFPACK_H */

/* Define to 1 if you have the <umfpack/umfpack.h> header file. */
/* #undef HAVE_UMFPACK_UMFPACK_H */

/* Define to 1 if you have the `uname' function. */
/* #undef HAVE_UNAME */

/* Define to 1 if you have the <unistd.h> header file. */
#define HAVE_UNISTD_H 1

/* Define to 1 if you have the `unlink' function. */
#define HAVE_UNLINK 1

/* Define to 1 if the system has the type `unsigned long long int'. */
#define HAVE_UNSIGNED_LONG_LONG_INT 1

/* Define if you have System V Release 3 signals. */
/* #undef HAVE_USG_SIGHOLD */

/* Define to 1 if you have the `usleep' function. */
#define HAVE_USLEEP 1

/* Define to 1 if you have the `utime' function. */
#define HAVE_UTIME 1

/* Define to 1 if you have the <utime.h> header file. */
#define HAVE_UTIME_H 1

/* Define to 1 if you have the <varargs.h> header file. */
/* #undef HAVE_VARARGS_H */

/* Define to 1 if you have the `vfprintf' function. */
#define HAVE_VFPRINTF 1

/* Define to 1 if you have the `vsnprintf' function. */
#define HAVE_VSNPRINTF 1

/* Define to 1 if you have the `vsprintf' function. */
#define HAVE_VSPRINTF 1

/* Define to 1 if you have the `waitpid' function. */
/* #undef HAVE_WAITPID */

/* Define to 1 if you have the <windows.h> header file. */
#define HAVE_WINDOWS_H 1

/* Define to 1 if you have the `x_utime' function. */
/* #undef HAVE_X_UTIME */

/* Define if you have X11 */
#define HAVE_X_WINDOWS 1

/* Define if ZLIB is available. */
#define HAVE_ZLIB 1

/* Define to 1 if you have the <zlib.h> header file. */
#define HAVE_ZLIB_H 1

/* Define to 1 if you have the `_chmod' function. */
#define HAVE__CHMOD 1

/* Define to 1 if you have the `_copysign' function. */
#define HAVE__COPYSIGN 1

/* Define to 1 if you have the `_finite' function. */
#define HAVE__FINITE 1

/* Define to 1 if you have the `_hypotf' function. */
/* #undef HAVE__HYPOTF */

/* Define to 1 if you have the `_isnan' function. */
#define HAVE__ISNAN 1

/* Define to 1 if you have the `_kbhit' function. */
#define HAVE__KBHIT 1

/* Define to 1 if you have the `_snprintf' function. */
#define HAVE__SNPRINTF 1

/* Define to 1 if you have the `_utime32' function. */
/* #undef HAVE__UTIME32 */

/* Define to 1 if octave index type is long */
/* #undef IDX_TYPE_LONG */

/* Define if host mkdir takes a single argument. */
#define MKDIR_TAKES_ONE_ARG 1

/* Define if signal handlers must be reinstalled after they are called. */
#define MUST_REINSTALL_SIGHANDLERS 1

/* Define if the QHull library needs a wh_version variable defined. */
/* #undef NEED_QHULL_VERSION */

/* Define if strptime is broken on your system */
#define OCTAVE_HAVE_BROKEN_STRPTIME 1

/* Define if this is Octave. */
#define OCTAVE_SOURCE 1

/* Define to the address where bug reports for this package should be sent. */
#define PACKAGE_BUGREPORT ""

/* Define to the full name of this package. */
#define PACKAGE_NAME ""

/* Define to the full name and version of this package. */
#define PACKAGE_STRING ""

/* Define to the one symbol short name of this package. */
#define PACKAGE_TARNAME ""

/* Define to the version of this package. */
#define PACKAGE_VERSION ""

/* Define to necessary symbol if this constant uses a non-standard name on
   your system. */
/* #undef PTHREAD_CREATE_JOINABLE */

/* Define as the return type of signal handlers (`int' or `void'). */
#define RETSIGTYPE void

/* Define if this if RETSIGTYPE is defined to be void. Needed because
   preprocessor comparisons to void fail on some systems. */
#define RETSIGTYPE_IS_VOID 1

/* Define if your struct rusage only has time information. */
/* #undef RUSAGE_TIMES_ONLY */

/* Define if using an SCO system. */
/* #undef SCO */

/* Define this to be the path separator for your system, as a character
   constant. */
#define SEPCHAR ';'

/* Define this to the path separator, as a string. */
#define SEPCHAR_STR ";"

/* The size of `int', as computed by sizeof. */
#define SIZEOF_INT 4

/* The size of `long', as computed by sizeof. */
#define SIZEOF_LONG 4

/* The size of `long double', as computed by sizeof. */
#define SIZEOF_LONG_DOUBLE 12

/* The size of `long long', as computed by sizeof. */
#define SIZEOF_LONG_LONG 8

/* The size of `short', as computed by sizeof. */
#define SIZEOF_SHORT 2

/* The size of `void *', as computed by sizeof. */
/* #undef SIZEOF_VOID_P */

/* To quiet autoheader. */
/* #undef SMART_PUTENV */

/* If using the C implementation of alloca, define if you know the
   direction of stack growth for your system; otherwise it will be
   automatically deduced at runtime.
	STACK_DIRECTION > 0 => grows toward higher addresses
	STACK_DIRECTION < 0 => grows toward lower addresses
	STACK_DIRECTION = 0 => direction of growth unknown */
/* #undef STACK_DIRECTION */

/* Define to 1 if you have the ANSI C header files. */
#define STDC_HEADERS 1

/* Define to 1 if you can safely include both <sys/time.h> and <time.h>. */
#define TIME_WITH_SYS_TIME 1

/* Define to 1 if your <sys/time.h> declares `struct tm'. */
/* #undef TM_IN_SYS_TIME */

/* Define if the UMFPACK Complex solver allow matrix and RHS to be split
   independently */
#define UMFPACK_SEPARATE_SPLIT 1

/* Define if using 64-bit integers for array dimensions and indexing */
/* #undef USE_64_BIT_IDX_T */

/* Define to use the readline library. */
#define USE_READLINE 1

/* Enable extensions on AIX 3, Interix.  */
#ifndef _ALL_SOURCE
# define _ALL_SOURCE 1
#endif
/* Enable GNU extensions on systems that have them.  */
#ifndef _GNU_SOURCE
# define _GNU_SOURCE 1
#endif
/* Enable threading extensions on Solaris.  */
#ifndef _POSIX_PTHREAD_SEMANTICS
# define _POSIX_PTHREAD_SEMANTICS 1
#endif
/* Enable extensions on HP NonStop.  */
#ifndef _TANDEM_SOURCE
# define _TANDEM_SOURCE 1
#endif
/* Enable general extensions on Solaris.  */
#ifndef __EXTENSIONS__
# define __EXTENSIONS__ 1
#endif


/* Define to 1 if `lex' declares `yytext' as a `char *' by default, not a
   `char[]'. */
/* #undef YYTEXT_POINTER */


#if defined (__cplusplus)
extern "C" {
#endif
#if HAVE_EXP2 && ! HAVE_DECL_EXP2
double exp2 (double );
#endif
#if HAVE_ROUND && ! HAVE_DECL_ROUND
double round (double);
#endif
#if HAVE_TGAMMA && ! HAVE_DECL_TGAMMA
double tgamma (double);
#endif
#if defined (__cplusplus)
}
#endif


/* Define if using HDF5 dll (Win32) */
/* #undef _HDF5USEDLL_ */

/* Define to 1 if on MINIX. */
/* #undef _MINIX */

/* Define to 2 if the system does not provide POSIX.1 features except with
   this defined. */
/* #undef _POSIX_1_SOURCE */

/* Define to 1 if you need to in order for `stat' and other things to work. */
/* #undef _POSIX_SOURCE */

/* Define if your system needs it to define math constants like M_LN2 */
/* #undef _USE_MATH_DEFINES */

/* Define to 0x0403 to access InitializeCriticalSectionAndSpinCount */
#define _WIN32_WINNT 0x0403

/* Define if your version of GNU libc has buggy inline assembly code for math
   functions like exp. */
#define __NO_MATH_INLINES 1

/* Define to empty if `const' does not conform to ANSI C. */
/* #undef const */

/* Define to `int' if <sys/types.h> doesn't define. */
#define gid_t int

/* Define to `int' if <sys/types.h> does not define. */
/* #undef mode_t */

/* Define to `long int' if <sys/types.h> does not define. */
/* #undef off_t */

/* Define to `int' if <sys/types.h> does not define. */
/* #undef pid_t */

/* Define to `unsigned int' if <sys/types.h> does not define. */
/* #undef size_t */

/* Define to `int' if <sys/types.h> doesn't define. */
#define uid_t int


#if defined (__GNUC__)
#define GCC_ATTR_DEPRECATED __attribute__ ((__deprecated__))
#define GCC_ATTR_NORETURN __attribute__ ((__noreturn__))
#define GCC_ATTR_UNUSED __attribute__ ((__unused__))
#else
#define GCC_ATTR_DEPRECATED
#define GCC_ATTR_NORETURN
#define GCC_ATTR_UNUSED
#endif

#define X_CAST(T, E) (T) (E)

#if defined (CXX_BROKEN_REINTERPRET_CAST)
#define FCN_PTR_CAST(T, E) (T) (E)
#else
#define FCN_PTR_CAST(T, E) reinterpret_cast<T> (E)
#endif

#if !defined(HAVE_DEV_T)
typedef short dev_t;
#endif

#if !defined(HAVE_INO_T)
typedef unsigned long ino_t;
#endif

#if !defined(HAVE_NLINK_T)
typedef short nlink_t;
#endif

#if !defined(HAVE_SIGSET_T)
typedef int sigset_t;
#endif

#if !defined(HAVE_SIG_ATOMIC_T)
typedef int sig_atomic_t;
#endif

#if defined (_MSC_VER)
#define __WIN32__
#define WIN32
/* missing parameters in macros */
#pragma warning (disable: 4003)
/* missing implementations in template instantiation */
#pragma warning (disable: 4996)
/* deprecated function names (FIXME?) */
#pragma warning (disable: 4661)
#endif

#if defined (__WIN32__) && ! defined (__CYGWIN__)
#define OCTAVE_HAVE_WINDOWS_FILESYSTEM 1
#elif defined (__CYGWIN__)
#define OCTAVE_HAVE_WINDOWS_FILESYSTEM 1
#define OCTAVE_HAVE_POSIX_FILESYSTEM 1
#else
#define OCTAVE_HAVE_POSIX_FILESYSTEM 1
#endif

/* Define if we expect to have <windows.h>, Sleep, etc. */
#if defined (__WIN32__) && ! defined (__CYGWIN__)
#define OCTAVE_USE_WINDOWS_API 1
#endif

#if defined (__APPLE__) && defined (__MACH__)
#define OCTAVE_USE_OS_X_API 1
#endif

/* sigsetjmp is a macro, not a function. */
#if defined (sigsetjmp) && defined (HAVE_SIGLONGJMP)
#define OCTAVE_HAVE_SIG_JUMP
#endif

#if defined (__DECCXX)
#define __USE_STD_IOSTREAM
#endif

#if defined (_UNICOS)
#define F77_USES_CRAY_CALLING_CONVENTION
#endif

#if 0
#define F77_USES_VISUAL_FORTRAN_CALLING_CONVENTION
#endif

#ifdef USE_64_BIT_IDX_T
#define SIZEOF_OCTAVE_IDX_TYPE 8
#else
#define SIZEOF_OCTAVE_IDX_TYPE SIZEOF_INT
#endif

// To be able to use long doubles for 64-bit mixed arithmetics, we need them at
// least 80 bits wide and we need roundl declared in math.h.
// FIXME -- maybe substitute this by a more precise check in the future.
#if (SIZEOF_LONG_DOUBLE >= 10) && defined (HAVE_ROUNDL)
#define OCTAVE_INT_USE_LONG_DOUBLE
#endif

#define OCTAVE_EMPTY_CPP_ARG

#include "oct-dlldefs.h"
#include "oct-types.h"

