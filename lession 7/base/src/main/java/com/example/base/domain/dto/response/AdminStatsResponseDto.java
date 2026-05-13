package com.example.base.domain.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminStatsResponseDto {
  long totalUsers;

  long totalAdmins;

  long totalInvalidatedTokens;
}
