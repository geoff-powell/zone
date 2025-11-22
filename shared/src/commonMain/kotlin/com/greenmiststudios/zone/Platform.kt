package com.greenmiststudios.zone

interface Platform {
  val name: String
}

expect fun getPlatform(): Platform