import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Abccovid19SharedModule } from 'app/shared/shared.module';

import { DocsComponent } from './docs.component';

import { docsRoute } from './docs.route';

@NgModule({
  imports: [Abccovid19SharedModule, RouterModule.forChild([docsRoute])],
  declarations: [DocsComponent]
})
export class DocsModule {}
